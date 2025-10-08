package com.app.todo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="access_right_mst")
public class AccessRight 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Role role;
    
    @ManyToOne
    private Form form;
    
    @ManyToOne
    private Permission permission;
    
    private Boolean hasAccess;  // This flag will store whether the role has access or not

	public Long getId() {
		return id;
	}

	public Role getRole() {
		return role;
	}

	public Form getForm() {
		return form;
	}

	public Permission getPermission() {
		return permission;
	}

	public Boolean getHasAccess() {
		return hasAccess;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public void setHasAccess(Boolean hasAccess) {
		this.hasAccess = hasAccess;
	}
}
