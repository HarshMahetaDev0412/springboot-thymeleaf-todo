package com.app.todo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_category_mst")
public class TaskCategory 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "category_cd")
	private Long categoryCd;
	
	@Column(name = "category_nm")
	private String name;
	
	@Column(name = "category_abbr")
	private String abbr;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "ent_dt")
	private LocalDateTime ent_dt;
	
	@Column(name = "mod_dt")
	private LocalDateTime mod_dt;
	
	public TaskCategory() {
		
	}
	
	public TaskCategory(String name, String abbr, String status, LocalDateTime ent_dt, LocalDateTime mod_dt)
	{
		this.name = name;
		this.abbr = abbr;
		this.status = status;
		this.ent_dt = ent_dt;
		this.mod_dt = mod_dt;
	}

	public Long getCategoryCd() {
		return categoryCd;
	}

	public String getName() {
		return name;
	}

	public String getAbbr() {
		return abbr;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getEnt_dt() {
		return ent_dt;
	}

	public LocalDateTime getMod_dt() {
		return mod_dt;
	}

	public void setCategoryCd(Long categoryCd) {
		this.categoryCd = categoryCd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setEnt_dt(LocalDateTime ent_dt) {
		this.ent_dt = ent_dt;
	}

	public void setMod_dt(LocalDateTime mod_dt) {
		this.mod_dt = mod_dt;
	}
}
