package com.app.todo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.todo.models.User;
import com.app.todo.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler
{
	private final UserRepository userRepository;

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException 
    {
    	 
    	 String username = authentication.getName();
    	 
    	 Optional<User> user = userRepository.findByEmail(username);

    	 User upUser = user.get();
    	 upUser.setLastLoggedIn(""+new Timestamp(System.currentTimeMillis()));
    	 
         // Save changes
    	 userRepository.save(upUser);

    	 System.out.println("✅ Logged in user email: " + username);
         System.out.println("✅ provider: " + "LOCAL");
         
    	 response.sendRedirect("/home/todo_home");  
    }
}
