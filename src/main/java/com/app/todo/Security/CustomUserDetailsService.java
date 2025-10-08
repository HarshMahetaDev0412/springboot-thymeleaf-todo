package com.app.todo.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.todo.models.User;
import com.app.todo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
        
        System.out.println("Loaded user: " + user.getEmail());
        System.out.println("User  roles size: " + user.getRoles().size());
        user.getRoles().forEach(role -> System.out.println("Role name: " + role.getName()));

        String[] roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new);

        System.out.println("Role names array: " + java.util.Arrays.toString(roleNames));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .roles(roleNames)
            .build();
    }

    
    // Spring Security calls this method when someone logs in
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//       
////    	User user = userRepository.findByEmail(email)
////            .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
//        
//        User user = userRepository.findByEmailWithRoles(email)  // Use the new method
//                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
//            
//        // Convert Set<Role> to String[]
//        String[] roleNames = user.getRoles().stream()
//        		.map(role -> role.getName()) // role.getName() returns something like "USER"
//        		.toArray(String[]::new);
//
//        // Debug: Log the role names array
//        System.out.println("Role names array: " + java.util.Arrays.toString(roleNames));
//        
//        return org.springframework.security.core.userdetails.User
//            .withUsername(user.getEmail()) // email is used as username
//            .password(user.getPassword()) // already hashed
//            .roles(roleNames)			 // Upward converted toString as roles has to be passed in String and Roles is a HashSet()
//            .build();
//    }
}
