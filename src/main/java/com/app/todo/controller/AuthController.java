package com.app.todo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.todo.models.AuthProvider;
import com.app.todo.models.Role;
import com.app.todo.repository.RoleRepository;
import com.app.todo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class AuthController 
{
	
	@Autowired
	private final UserService userService;
	private final RoleRepository roleRepository;
	
	public AuthController (UserService userService, RoleRepository roleRepository) 
	{
		this.userService = userService;
		this.roleRepository = roleRepository;
	}
	
	
	@GetMapping("/auth/login")
	public String loginPageHandller() 
	{
		return "auth/login";
	}
	
	@GetMapping("/auth/register")
	public String registerPageHandller(HttpServletRequest request) 
	{
		return "auth/register";
	}
	
	@PostMapping(value = "/do-register")
	public String processRegister(@RequestParam(required = true, name = "username") String username,
	        @RequestParam(required = true, name = "email") String email,
	        @RequestParam(required = true) String password,
	        RedirectAttributes redirectAttributes) {
	    
	    String url = "";
	    
	    try {
	        // Fetch the default USER role
	        Role userRole = roleRepository.findByName("USER")
	                .orElseThrow(() -> new RuntimeException("USER role not found"));
	        
	        // Create Set and ADD the role (this was missing!)
	        Set<Role> roles = new HashSet<>();
	        roles.add(userRole); // Now it's populated: [USER]
	        
	        // Check if user exists
	        Boolean userExists = userService.checkUserExistsByEmail(email.trim());
	        
	        if (!userExists) {
	            // Register with populated roles
	            userService.registerUser (username.trim(), email.trim(), password, AuthProvider.LOCAL, roles);
	            
	            System.out.println("User  Registered: " + email + " with role: USER"); // Better logging
	            redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
	            url = "redirect:/auth/login";
	        } else {
	            System.out.println("User  Exists: " + email); // Better logging
	            redirectAttributes.addFlashAttribute("message", "Looks like you already have an account. Welcome back!");
	            url = "redirect:/auth/register";
	        }
	    } catch (RuntimeException e) {
	        System.err.println("Registration error: " + e.getMessage()); // Log errors
	        redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
	        url = "redirect:/auth/register";
	    } catch (Exception e) {
	        System.err.println("Unexpected registration error: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
	        url = "redirect:/auth/register";
	    }
	    
	    return url;
	}

	
//	//Processing Register 
//	@PostMapping(value = "/do-register")
//	public String processRegister(@RequestParam(required = true, name="username") String username,
//			@RequestParam(required = true, name="email") String email,
//			@RequestParam(required = true) String password,
//			RedirectAttributes redirectAttributes) 
//	{
//		String url="";
//		
//		Set<Role> roles =new HashSet<>();
//		
//		Role userRole = roleRepository.findByName("USER")
//                .orElseThrow(() -> new RuntimeException("USER role not found"));
//		
//		Boolean userExists = userService.checkUserExistsByEmail(email.trim());
//		
//		if(!userExists) 
//		{
//			userService.registerUser(username.trim(), email.trim(), password, AuthProvider.LOCAL, roles);
//			
//			System.out.println("USer Registered!!");
//			url = "redirect:/auth/login";
//		}
//		else
//		{
//			System.out.println("USer Exists!!");
//			
//			redirectAttributes.addFlashAttribute("message", "Looks like you already have an account. Welcome back!");
//			url = "redirect:/auth/register";
//		}
//		return url;
//	}
}
