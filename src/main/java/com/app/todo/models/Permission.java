package com.app.todo.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="permission_mst")
public class Permission 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="permission_id")
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	private String description;
	
	@ManyToMany(mappedBy = "permissions")
	private Set<Role> roles;
	
	// Constructors
    public Permission() {}
    
    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
