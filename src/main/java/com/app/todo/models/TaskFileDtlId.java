package com.app.todo.models;

import java.io.Serializable;
import java.util.Objects;

public class TaskFileDtlId implements Serializable
{
	private Task task;
	private Integer fileSeq;
	
	public TaskFileDtlId() {}

    // Parameterized constructor to initialize both fields
    public TaskFileDtlId(Task task, Integer fileSeq) {
        this.task = task;
        this.fileSeq = fileSeq;
    }

	public Task getTask() {
		return task;
	}

	public Integer getFileSeq() {
		return fileSeq;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
	}
    
	// Override equals() and hashCode() to ensure proper comparison and hashing
	//These methods are overridden to ensure that comparisons of composite keys are handled correctly.
	
	
	//equals(): Checks if two composite keys are equal by comparing both the task and fileSeq fields.
	@Override
	public boolean equals(Object o) 
	{
		if(this == o) return true;
		
		if(o == null || getClass() != o.getClass()) return false;
		
		TaskFileDtlId tfdi = (TaskFileDtlId) o;
		
		return Objects.equals(tfdi, tfdi.task) && Objects.equals(tfdi, tfdi.fileSeq);
	}
	
	//hashCode(): Generates a hash value based on both fields. 
				//This is necessary for using the composite key in collections like HashMap and HashSet and for efficient comparison.
	@Override
    public int hashCode() {
        return Objects.hash(task, fileSeq);
    }

}
