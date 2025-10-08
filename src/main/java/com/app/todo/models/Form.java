package com.app.todo.models;

import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="form_mst")
public class Form 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long form_cd;
	@Nonnull
	@Column(name = "module_cd") // keeps DB column the same
	private Long moduleCd;
	@Nonnull
	//private String form_nm;
	private String type;
	private String path;
	private Boolean flag;
	private Long sub_menu_cd;
	private String sub_menu_nm;
	//private Long sub_menu_seq;
	private LocalDateTime ent_dt;
	private LocalDateTime mod_dt;
	@Column(name = "sub_menu_seq")
	private Long subMenuSeq;

	@Column(name = "form_nm")
	private String formNm;

	
	public Form() {}

	public Form(Long moduleCd, String form_nm, String type, String path, Boolean flag, Long sub_menu_cd, String sub_menu_nm, LocalDateTime ent_dt, LocalDateTime mod_dt, Long sub_menu_seq) 
	{
		this.moduleCd =moduleCd;
		this.formNm = form_nm;
		this.type = type;
		this.path = path;
		this.flag = flag;
		this.sub_menu_cd = sub_menu_cd;
		this.sub_menu_nm = sub_menu_nm;
		this.subMenuSeq = sub_menu_seq;
		this.ent_dt = ent_dt;
		this.mod_dt = mod_dt;
	}

	public Long getForm_cd() {
		return form_cd;
	}

	public Long getModuleCd() {
		return moduleCd;
	}

	public String getType() {
		return type;
	}

	public String getPath() {
		return path;
	}

	public Boolean getFlag() {
		return flag;
	}

	public Long getSub_menu_cd() {
		return sub_menu_cd;
	}

	public String getSub_menu_nm() {
		return sub_menu_nm;
	}

	public LocalDateTime getEnt_dt() {
		return ent_dt;
	}

	public LocalDateTime getMod_dt() {
		return mod_dt;
	}

	public void setForm_cd(Long form_cd) {
		this.form_cd = form_cd;
	}

	public void setModuleCd(Long moduleCd) {
		this.moduleCd = moduleCd;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public void setSub_menu_cd(Long sub_menu_cd) {
		this.sub_menu_cd = sub_menu_cd;
	}

	public void setSub_menu_nm(String sub_menu_nm) {
		this.sub_menu_nm = sub_menu_nm;
	}

	public void setEnt_dt(LocalDateTime ent_dt) {
		this.ent_dt = ent_dt;
	}

	public void setMod_dt(LocalDateTime mod_dt) {
		this.mod_dt = mod_dt;
	}
	
	public Long getSubMenuSeq() { return subMenuSeq; }
	public void setSubMenuSeq(Long subMenuSeq) { this.subMenuSeq = subMenuSeq; }

	public String getFormNm() { return formNm; }
	public void setFormNm(String formNm) { this.formNm = formNm; }

}
