package com.app.todo.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.app.todo.dto.TaskRepository;
import com.app.todo.models.Task;

@Component
public class TaskSpecifications 
{
	private final TaskRepository taskRepository;
	
	public TaskSpecifications(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) 
	{
		this.taskRepository = taskRepository;
	}
	
	
	public List<Task> filterTasks(Integer categoryCd, String allCompleted, String from_dt, String to_dt) throws ParseException {
	    Boolean completed = "on".equals(allCompleted) ? true : null;

	    Specification<Task> spec = Specification.where(null);

	    if (categoryCd != null && categoryCd != 0) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("categoryCd"), categoryCd));
	    }

	    if (completed != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("completed"), completed));
	    }

	    if (from_dt != null && !from_dt.isEmpty()) {

	    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    	Date fromDate = formatter.parse(from_dt);
	        
	        spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueDt"), fromDate));
	    }

	    if (to_dt != null && !to_dt.isEmpty()) {

	    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    	Date toDate = formatter.parse(to_dt);
	    	
	        spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueDt"), toDate));
	    }
	    return taskRepository.findAll(spec);
	}
}
