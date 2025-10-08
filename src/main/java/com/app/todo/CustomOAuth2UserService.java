package com.app.todo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.app.todo.models.AuthProvider;
import com.app.todo.models.Role;
import com.app.todo.models.User;
import com.app.todo.repository.RoleRepository;
import com.app.todo.repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        // Access user info from token
        Map<String, Object> attributes = oidcUser.getAttributes();
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        // inside your method
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ✅ Get provider from the userRequest, not SecurityContext
        String provider = userRequest.getClientRegistration().getRegistrationId(); // <-- "google" or "github"

        System.out.println("✅ Logged in user email: " + email);
        System.out.println("✅ provider: " + provider);
        
        // Save or update user in your DB
        User user = userRepository.findByEmailWithRoles(email).map(existingUser -> {
        	
        	existingUser.setLastLoggedIn(new Timestamp(System.currentTimeMillis()).toString());
        	return userRepository.save(existingUser);

        }) .orElseGet(() -> {
            User newUser = new User();
            
            newUser.setAuthProvider(AuthProvider.valueOf(provider.toUpperCase()));
            newUser.setName(name);
            newUser.setEntDt(new Timestamp(System.currentTimeMillis()));
            newUser.setEmail(email);
            newUser.setStatus("A");
            newUser.setLastLoggedIn(""+new Timestamp(System.currentTimeMillis()));
            
            // 1. Fetch the default USER role from DB
            Role userRole = roleRepository.findByName("USER")
                                 .orElseThrow(() -> new RuntimeException("USER role not found"));

            // 2. Assign it as a Set
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            newUser.setRoles(roles);
            
            return userRepository.save(newUser);
        });
        
        // 1. Map user's DB roles to Spring Security authorities
        Set<GrantedAuthority> mappedAuthorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toSet());

        // 2. Return a new OidcUser with your mapped authorities
        return new DefaultOidcUser(
            mappedAuthorities,
            oidcUser.getIdToken(),
            oidcUser.getUserInfo(),
            "email"  // Use "email" as the userNameAttributeKey
        );

		/*
		 * // ✅ Important: preserve authorities return new DefaultOidcUser(
		 * oidcUser.getAuthorities(), // retain authorities (like SCOPE_openid, etc.)
		 * oidcUser.getIdToken(), // required for OIDC oidcUser.getUserInfo(), //
		 * optional but recommended "email" // use "email" as the name attribute key );
		 */
    }
}
