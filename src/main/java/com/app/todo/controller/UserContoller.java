package com.app.todo.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.todo.models.AuthProvider;
import com.app.todo.models.Role;
import com.app.todo.models.User;
import com.app.todo.repository.AdminSpecifications;
import com.app.todo.services.UserService;

@Controller
public class UserContoller 
{
	private final UserService userService;
	private final AdminSpecifications adminSpecifications;
	
	public UserContoller(UserService userService, AdminSpecifications adminSpecifications) 
	{
		this.userService = userService;
		this.adminSpecifications = adminSpecifications;
	}
	
	
	
	@GetMapping("/admin/frm_user_mgmt")
	public String getUserMgmtDtl(Model model,
			@RequestParam(required = false, defaultValue = "0") String filt_provider, Principal principal) throws ParseException 
	{
	    
		Optional<User> user = userService.findByEmail(principal.getName());	
		
		User getUser = user.get();
		
		 Set<Role> roles = getUser.getRoles();
		
		List<User> users = adminSpecifications.filterUsers(filt_provider, roles);//userService.getAllUSersList();
		
		AuthProvider[] providers = AuthProvider.values(); 
		
		model.addAttribute("users", users);
		model.addAttribute("providers", providers);
		model.addAttribute("filt_provider", filt_provider);
		model.addAttribute("users_size", users.size());
		
		
		return "/admin/frm_user_mgmt";
	}

	@PostMapping("/admin/frm_user_mgmt")
	public String postUserMgmtDtl(Model model) 
	{
		return "redirect:/admin/frm_user_mgmt";
	}

	@GetMapping("/toggle-user/status")
	public String toggleUserStatus(@RequestParam Long id, @RequestParam String status_val, RedirectAttributes redirectAttribute) 
	{
		String stausName = status_val.equals("Y") ? "Active" : "Inactive";
		userService.toggleStatus(id, status_val);
		redirectAttribute.addFlashAttribute("message", "Successfully - User status set to "+stausName+"!!");
		
		return "redirect:/admin/frm_user_mgmt";
	}
}
