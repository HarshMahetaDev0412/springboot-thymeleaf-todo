package com.app.todo.models;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data //This is lombark annotation used to create constructors like getters and setters (Which reduces the boilerplate code)
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private Boolean completed;
	private String description;
	private String priority;
	
	@Column(name="is_attached")
	private Boolean isAttached = false; // Default value in Java
	
	@ManyToOne(fetch = FetchType.EAGER)//This is to make sure the category is fetched immediately with the task, avoiding lazy loading issues in the view.
    @JoinColumn(name = "category_cd", referencedColumnName = "category_cd")
	private TaskCategory category;

	private String email;
	
	@Column(name="due_dt")
	private Date dueDt;
	private LocalDateTime ent_dt;
	private LocalDateTime mod_dt;
	
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public boolean isCompleted() {
		return completed;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getEnt_dt() {
		return ent_dt;
	}
	public LocalDateTime getMod_dt() {
		return mod_dt;
	}
	
	//Setters
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setEnt_dt(LocalDateTime ent_dt) {
		this.ent_dt = ent_dt;
	}
	public void setMod_dt(LocalDateTime mod_dt) {
		this.mod_dt = mod_dt;
	}
	public TaskCategory getCategory() {
		return category;
	}
	public void setCategory(TaskCategory category) {
		this.category = category;
	}
	public String getPriority() {
		return priority;
	}
	public Date getDueDt() {
		return dueDt;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
	}
	public Boolean isAttached() {
		return isAttached;
	}
	public String getEmail() {
		return email;
	}
	public void setAttached(Boolean isAttached) {
		this.isAttached = isAttached;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
