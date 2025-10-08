package com.app.todo.repository;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.app.todo.models.Role;
import com.app.todo.models.User;

@Component
public class AdminSpecifications
{
	private final UserRepository userRepository;
	
	public AdminSpecifications(UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}
	
	public List<User> filterUsers(String authProvider, Set<Role> loggedUSerRole) throws ParseException
	{
		Specification<User> specs = Specification.where(null);
		
		if(!authProvider.equals("") && !authProvider.equals("0")) 
		{
			specs = specs.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("authProvider"), authProvider)));
		}
		
		if(loggedUSerRole.contains("USER"))
		{
			specs = specs.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roles"), loggedUSerRole)));
		}
		
		return userRepository.findAll(specs, Sort.by(Sort.Order.asc("roles")));
		
	}
}
