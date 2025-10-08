package com.app.todo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "module_mst")
public class ModuleEntity
{
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long module_cd;
	private String module_name;
	private Integer priority;
	private Boolean flag;
	private LocalDateTime ent_dt;
	private LocalDateTime mod_dt;

	
	//Constructor
	public ModuleEntity()
	{
		
	}

	public ModuleEntity(String module_name, Boolean flag, LocalDateTime ent_dt, LocalDateTime mod_dt, Integer priority)
	{
		this.module_name = module_name;
		this.flag = flag;
		this.ent_dt = ent_dt;
		this.mod_dt = mod_dt;
		this.priority = priority;
	}

	public Long getModule_cd() {
		return module_cd;
	}

	public String getModule_name() {
		return module_name;
	}

	public Boolean getFlag() {
		return flag;
	}

	public LocalDateTime getEnt_dt() {
		return ent_dt;
	}

	public LocalDateTime getMod_dt() {
		return mod_dt;
	}

	public void setModule_cd(Long module_cd) {
		this.module_cd = module_cd;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public void setEnt_dt(LocalDateTime ent_dt) {
		this.ent_dt = ent_dt;
	}

	public void setMod_dt(LocalDateTime mod_dt) {
		this.mod_dt = mod_dt;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
