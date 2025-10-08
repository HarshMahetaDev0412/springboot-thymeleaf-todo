package com.app.todo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="task_file_dtl")
@IdClass(TaskFileDtlId.class)  // Define the composite key class
public class TaskFileDtl 
{
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="task_id", referencedColumnName = "id")
	private Task task;
	
	@Id
	@Column(name="file_seq")
	private Integer fileSeq;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="ent_dt")
	private LocalDateTime entDt;
	
	public TaskFileDtl() {
		
	}
	
	public TaskFileDtl(String fileName, LocalDateTime entDt) 
	{
		this.fileName = fileName;
		this.entDt = entDt;
	}

	public Task getTask() {
		return task;
	}

	public Integer getFileSeq() {
		return fileSeq;
	}

	public String getFileName() {
		return fileName;
	}

	public LocalDateTime getEntDt() {
		return entDt;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setEntDt(LocalDateTime entDt) {
		this.entDt = entDt;
	}
}
