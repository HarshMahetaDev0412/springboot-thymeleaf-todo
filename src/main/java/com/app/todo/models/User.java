package com.app.todo.models;

import java.sql.Timestamp;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_mst")
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password; // Will be null for OAuth users
	
	@Enumerated(EnumType.STRING)
	private AuthProvider authProvider; //LOCAL, GOOGLE, GITHUB
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="user_role",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Role> roles;
	
	private String lastLoggedIn;
	private String status;
	
	private Timestamp entDt;
	private Timestamp modDt;
	
	// Helper method to check if user has a specific role
	public boolean hasRole(String roleName)
	{
		return roles.stream().anyMatch(role -> role.getName().equals(roleName));
	}
	
	// Helper method to check if user has a specific permission
	public boolean hasPermission(String permissionName) 
	{
		return roles.stream()
				.flatMap(role -> role.getPermissions().stream())
				.anyMatch(permission -> permission.getName().equals(permissionName));
	}
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public AuthProvider getAuthProvider() {
		return authProvider;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public String getLastLoggedIn() {
		return lastLoggedIn;
	}
	public String getStatus() {
		return status;
	}
	public Timestamp getEntDt() {
		return entDt;
	}
	public Timestamp getModDt() {
		return modDt;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void setLastLoggedIn(String lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setEntDt(Timestamp entDt) {
		this.entDt = entDt;
	}
	public void setModDt(Timestamp modDt) {
		this.modDt = modDt;
	}
	
}
