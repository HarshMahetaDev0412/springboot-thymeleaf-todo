package com.app.todo.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.todo.models.AuthProvider;
import com.app.todo.models.Role;
import com.app.todo.models.User;
import com.app.todo.repository.RoleRepository;
import com.app.todo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService 
{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) 
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public Boolean checkUserExistsByEmail(String email)
	{
		Boolean userExists = userRepository.existsByEmail(email);
		return userExists;
	}

	@Transactional // Ensures user + roles are saved atomically
    public void registerUser (String username, String email, String password, AuthProvider provider, Set<Role> roles) {
        // Double-check no duplicate (redundant but safe)
        if (checkUserExistsByEmail(email)) {
            throw new RuntimeException("User  already exists with email: " + email);
        }
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // CRITICAL: Hash the password!
        user.setAuthProvider(provider);
        user.setStatus("A");
        user.setRoles(roles); // Now receives populated Set<Role>
        User savedUser  = userRepository.save(user);
        System.out.println("Service: Saved user ID " + savedUser .getId() + " with " + roles.size() + " roles"); // Debug log
    }

	public Optional<User> findByEmail(String username)
	{
		Optional<User> user = userRepository.findByEmail(username);
		return user;
	}

	/*
	 * public List<User> getAllUSersList() { List<User> users =
	 * userRepository.findAllByRoles("USER");
	 * 
	 * return users; }
	 */
	public void toggleStatus(Long id, String status_val) 
	{
		Optional<User> user = userRepository.findById(id);
		
		User changeUSer = user.get();
		changeUSer.setStatus(status_val);
		userRepository.save(changeUSer);
	}

	public List<Role> getAllRoles() 
	{
		List<Role> roles = roleRepository.findAll();
		return roles;
	}

	public Optional<Role> getRoleById(Long group_cd) {

		return roleRepository.findById(group_cd);
	}
}
