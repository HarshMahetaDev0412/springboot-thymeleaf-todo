package com.app.todo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.app.todo.Security.CustomUserDetailsService;
import com.app.todo.models.AccessRight;
import com.app.todo.models.Form;
import com.app.todo.models.Role;
import com.app.todo.repository.AccessRightRepository;
import com.app.todo.repository.RoleRepository;
import com.app.todo.services.FormService;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Enabled method-level security with annotations like @PreAuthorize
public class SecurityConfig 
{

	private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final FormService formService;
    private final AccessRightRepository accessRightRepository;
    private final RoleRepository roleRepository;
    
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomOAuth2UserService customOAuth2UserService,
                          CustomLoginSuccessHandler customLoginSuccessHandler,
                          RoleRepository roleRepository,
                          AccessRightRepository accessRightRepository,
                          FormService formService) {
        this.userDetailsService = userDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.roleRepository = roleRepository;
        this.accessRightRepository = accessRightRepository;
        this.formService = formService;
    }
    
    /*
	 * CSRF (Cross-Site Request Forgery) is a type of security attack where a
	 * malicious website tricks a user's browser into performing an action on
	 * another site where the user is authenticated. For example, if you're logged
	 * into your banking site and then visit a malicious site, that malicious site
	 * could cause your browser to make a request (like transferring money) to your
	 * bank—without your consent or knowledge.
	 * 
	 * Spring Security includes built-in protection against CSRF by default.
	 */
  
    /*
    | You want...                    | Use this:                      |
    | ------------------------------ | ------------------------------ |
    | Just logged in                 | `.authenticated()`             |
    | Logged in AND has role         | `.hasRole("ADMIN")`            |
    | Anonymous (not logged in)      | `.anonymous()`                 |
    | Anyone (logged in or not)      | `.permitAll()`                 |
    | Must have any of several roles | `.hasAnyRole("ADMIN", "USER")` |
     */
    
    private List<Role> roles;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
    	updateRoleMappings();  // Update the role mappings every time the filter chain is created
    	
    	System.out.println("roles---->"+roles);//Not reaching here 
    	http
            .csrf().disable()//See up for explination
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("/auth/login", "/auth/register", "/do-login", "/do-register", "/css/**", "/js/**", "/oauth2/**", "/login/oauth2/**", "/auth/logout")
                        .permitAll(); // public URLs

                // Dynamically set role-based access
                Map<String, Set<String>> pathToRoles = new HashMap<>();
	            
                // 1. Collect all paths and their associated roles
                roles.forEach(role -> {
                	List<String> paths = getFormPathsForRole(role);
                	paths.forEach(path -> {
                		pathToRoles.computeIfAbsent(path, k -> new HashSet<>()).add(role.getName());
                	});
                });
                
                // 2. Now apply matchers per path, allowing multiple roles
                pathToRoles.forEach((path, roleNames) -> {
                	RequestMatcher matcher = new AntPathRequestMatcher(path);
                	String[] roleArray = roleNames.toArray(new String[0]);
                	
                	// Convert to role expressions like hasAnyRole("ADMIN", "MANAGER", ...)
                	auth.requestMatchers(matcher).hasAnyRole(roleArray);
                	
                	System.out.println("Applied path: " + path + " to roles: " + Arrays.toString(roleArray));
                });

                Map<String, Set<String>> notAccessedPaths = new HashMap<>();
                List<String> paths = getFormNotAccessed();
                paths.forEach(path -> {
                    RequestMatcher matcher = new AntPathRequestMatcher(path);
                    auth.requestMatchers(matcher).denyAll();
                    System.out.println("Denied access to path: " + path);
                });

                auth.anyRequest().authenticated(); // everything else needs authentication
            })
            .formLogin(login -> login
                .loginPage("/auth/login") // my custom login page
                .loginProcessingUrl("/do-login") // form submits here (Spring handles auth)
                //.defaultSuccessUrl("/home/todo_home", true) // where to go after login
                .successHandler(customLoginSuccessHandler)
                //.failureUrl("/auth/login?error=true") // if login fails
                .failureHandler(customeAuthenticationFailureHandler())
                .permitAll()
            )
//            .oauth2Login(oauth -> oauth
//            	    .loginPage("/auth/login")
//            	    .userInfoEndpoint(userInfo -> userInfo
//            	    		.oidcUserService(customOAuth2UserService)
//            	    )
//            	    .defaultSuccessUrl("/home/todo_home", true)
//            	)
            
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
//            .sessionManagement()
//            .invalidSessionUrl("/auth/login?invalidSession=true")
//            .maximumSessions(1)
//            .maxSessionsPreventsLogin(true)  // Prevents multiple logins with the same user session
//            .expiredUrl("/auth/login?sessionExpired=true");
            ;

        return http.build();
    }

    @EventListener
    public void onAccessRightsUpdated(AccessRightsUpdatedEvent event) {
        // Update role mappings
        updateRoleMappings(); 

        // Get the current authentication token from SecurityContext
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // Fetch updated roles for the user
        List<GrantedAuthority> updatedAuthorities = getUpdatedAuthorities(currentAuth.getName());

        // Create a new authentication token with updated roles
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                currentAuth.getPrincipal(),
                currentAuth.getCredentials(),
                updatedAuthorities
        );

        // Set the updated Authentication back into the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Debugging: Check if the updated authorities are correct
        System.out.println("Updated Authorities: " + updatedAuthorities);

        // Optionally, if you're using session-based security, you can refresh the session or invalidate it:
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if (session != null) {
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        }
    }

    private List<GrantedAuthority> getUpdatedAuthorities(String username) {
        // Fetch the updated roles for the user (this can be done by calling your role repository)
        List<Role> updatedRoles = roleRepository.findByEmailWithRoles(username);

        // Convert roles to GrantedAuthority
        return updatedRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Correct way: use role.getName()
                .collect(Collectors.toList());
    }
    
    // Updates role-based access configurations when the access rights are updated
    private void updateRoleMappings() 
    {
        this.roles = roleRepository.findAll();  // Reload roles from the repository
        System.out.println("updateRoleMappings Called ");
    }
    
    private List<String> getFormPathsForRole(Role role) {
      
    	//List<AccessRight> accessRights = accessRightRepository.findDistinctByRoleAndHasAccessTrue(role);
    	Set<AccessRight> accessRights = new HashSet<>(accessRightRepository.findByRoleAndHasAccessTrue(role));
        
        // Map AccessRight entities to their respective form paths
        return accessRights.stream()
            .map(accessRight -> accessRight.getForm().getPath()) // Assuming getPath() gives the form path
            .collect(Collectors.toList());
    }

    private List<String> getFormNotAccessed() {
    	
    	Set<Form> nonAccessedForms = new HashSet<>(formService.findFormNotAccessed());
    	
    	// Map AccessRight entities to their respective form paths
    	return nonAccessedForms.stream()
    			.map(nonAccessedForm -> nonAccessedForm.getPath()) // Assuming getPath() gives the form path
    			.collect(Collectors.toList());
    }

	@Bean
    public AuthenticationFailureHandler customeAuthenticationFailureHandler() 
    {
    	SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/error/globalError");
    	return handler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Optional: Authentication provider using your userDetailsService and password encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // from your Step 2
        provider.setPasswordEncoder(passwordEncoder()); // use BCrypt
        return provider;
    }
}
