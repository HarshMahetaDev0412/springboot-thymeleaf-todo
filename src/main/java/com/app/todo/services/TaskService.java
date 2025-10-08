package com.app.todo.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.todo.dto.TaskRepository;
import com.app.todo.models.Task;
import com.app.todo.models.TaskCategory;
import com.app.todo.models.TaskFileDtl;
import com.app.todo.repository.TaskCategoryRepository;
import com.app.todo.repository.TaskFileDtlRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final TaskCategoryRepository taskCategoryRepository;
	private final TaskFileDtlRepository taskFileDtlRepository;
	
	public TaskService(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository, TaskFileDtlRepository taskFileDtlRepository) 
	{
		this.taskRepository = taskRepository;
		this.taskCategoryRepository = taskCategoryRepository;
		this.taskFileDtlRepository = taskFileDtlRepository;
	}
	
	public List<Task> getAlltasks() {

		return taskRepository.findAllByOrderByTitleAsc();
	}

	public void createTask(String title, String desc, TaskCategory category_cd, LocalDateTime ent_dt, String priority, Date dueDt, Boolean isAttached, String email) {

		Task task = new Task();
		
		task.setTitle(title);
		task.setDescription(desc);
		task.setCompleted(false);
		task.setCategory(category_cd);
		task.setPriority(priority);
		task.setDueDt(dueDt);
		task.setAttached(isAttached);
		task.setEmail(email);
		task.setEnt_dt(ent_dt);
		taskRepository.save(task);
	}

	public void importTask(String title) {
		
		Task task = new Task();
		
		task.setTitle(title);
		//task.setCompleted(isCompleted);
		task.setCompleted(false);
		taskRepository.save(task);
	}

	public void deleteTask(Long id) 
	{
		taskRepository.deleteById(id);
	}

	public void toggleTask(Long id) 
	{
		Task task = taskRepository.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("No task found for!!"));
				
		task.setCompleted(!task.isCompleted());
		taskRepository.save(task);
	}

	public void updatetaskTitle(Long id, String title, String desc, TaskCategory category_cd, LocalDateTime mod_dt, String priority, Date dueDt, Boolean isAttached, String email) 
	{
		Task task = taskRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("No task found!!"));
		task.setTitle(title);
		task.setDescription(desc);
		task.setCategory(category_cd);
		task.setMod_dt(mod_dt);
		task.setPriority(priority);
		task.setDueDt(dueDt);
		task.setAttached(isAttached);
		task.setEmail(email);
		taskRepository.save(task);
	}

	public List<Task> getCompletedTasks(boolean isCompleted) 
	{
		return taskRepository.findAllByCompletedOrderByTitleAsc(isCompleted);
	}

	public List<TaskCategory> getAlltasksCategory() {

		return taskCategoryRepository.findAll();
	}

	public List<TaskCategory> getAllactiveTasksCategory() {
		
		return taskCategoryRepository.findAllByStatus("Y");
	}

	public void createTaskCategory(String category_nm, String category_abbr, String status_flag, LocalDateTime ent_dt) {
		
		TaskCategory taskCategory = new TaskCategory();
		
		taskCategory.setName(category_nm);
		taskCategory.setAbbr(category_abbr);
		taskCategory.setStatus(status_flag);
		taskCategory.setEnt_dt(ent_dt);
		
		taskCategoryRepository.save(taskCategory);
	}

	public void updateTaskCategory(RedirectAttributes redirectAttributes,Long category_cd, String category_nm, String category_abbr, String status_flag,
			LocalDateTime mod_dt) {
		
		TaskCategory taskCategory = taskCategoryRepository.findById(category_cd).orElse(null);
		
		if(taskCategory != null)
		{
			taskCategory.setName(category_nm);
			taskCategory.setAbbr(category_abbr);
			taskCategory.setStatus(status_flag);
			taskCategory.setEnt_dt(mod_dt);
			
			taskCategoryRepository.save(taskCategory);
		}
		else
		{
			redirectAttributes.addFlashAttribute("message", "Error - "+category_nm+" Can not be found!");
		}
	}

	public TaskCategory getCategoryById(Long categoryCd) 
	{
		// TODO Auto-generated method stub
		return taskCategoryRepository.findById(categoryCd).orElse(null);
	}
	
	public void addTaskFileDtls(Long task_id, String fileName) {
	    
		Task task = taskRepository.findById(task_id).orElse(new Task());
		
		int nextFileSeq = taskFileDtlRepository.findMaxFileSeqByTaskId(task.getId()) + 1;
		
	    TaskFileDtl taskFileDtl = new TaskFileDtl();
	    taskFileDtl.setTask(task);
	    taskFileDtl.setFileSeq(nextFileSeq); // Manually set
	    taskFileDtl.setFileName(fileName);
	    taskFileDtl.setEntDt(LocalDateTime.now());

	    taskFileDtlRepository.save(taskFileDtl);
	}
	
	public Optional<TaskFileDtl> getLatestFileForTask(Long taskId)
	{
	    return taskFileDtlRepository.findLatestFileByTaskId(taskId);
	}
}
