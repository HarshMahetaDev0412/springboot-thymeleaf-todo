package com.app.todo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.todo.models.Task;
import com.app.todo.models.TaskCategory;
import com.app.todo.models.TaskFileDtl;
import com.app.todo.repository.TaskSpecifications;
import com.app.todo.services.TaskService;
import com.app.todo.util.DateUtil;
import com.app.todo.util.commonVariable;

@Controller
public class TaskController 
{
	@Autowired
	private final TaskService taskService;
	@Autowired
	private final TaskSpecifications taskSpecifications;
	
	public TaskController(TaskService taskService, TaskSpecifications taskSpecifications) 
	{
		this.taskService = taskService;
		this.taskSpecifications = taskSpecifications;
	}

	DateUtil dateUtil = new DateUtil();
	
	@GetMapping("/task_mgmt/tasks")
	public String getTasks(@RequestParam(required = false) String allCompleted, 
			@RequestParam(defaultValue = "0") Integer filt_category_cd,
			@RequestParam(required = false) String from_dt,
			@RequestParam(required = false) String to_dt, 
			Model model,
			Authentication authentication) throws IOException, ParseException
	{
		
		from_dt = (from_dt == null)?dateUtil.getFirstDateOfMonth():from_dt;
		to_dt = (to_dt == null)?dateUtil.getSysdate():to_dt;
		
		List<Task> tasks = taskService.getAlltasks();
		List<TaskCategory> task_categories = taskService.getAllactiveTasksCategory();
		
		tasks = taskSpecifications.filterTasks(filt_category_cd, allCompleted, from_dt, to_dt);
		
		String projectRootPath = new File(".").getCanonicalPath();
		String dwn_path = projectRootPath + File.separator +commonVariable.taskFileDir +File.separator;
		
		model.addAttribute("tasks",tasks);
		model.addAttribute("tasks_size",tasks.size());
		model.addAttribute("allCompleted", allCompleted);
		model.addAttribute("task_categories", task_categories);
		model.addAttribute("filt_category_cd", filt_category_cd);
		model.addAttribute("from_dt", from_dt);
		model.addAttribute("to_dt", to_dt);
		model.addAttribute("dwn_path", dwn_path);
		return "task_mgmt/tasks";
	}
	
	@GetMapping("/task/download-latest/{taskId}")
	public ResponseEntity<org.springframework.core.io.Resource> downloadLatestTaskFile(@PathVariable Long taskId) throws IOException 
	{
		Optional<TaskFileDtl> optionalDtl = taskService.getLatestFileForTask(taskId);

	    if (optionalDtl.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    TaskFileDtl fileDtl = optionalDtl.get();
	    String fileName = fileDtl.getFileName();

	    // Resolve file path (same logic as upload)
	    String projectRootPath = new File(".").getCanonicalPath();
	    String relativePath = commonVariable.taskFileDir;
	    Path filePath = Paths.get(projectRootPath, relativePath, fileName);

	    if (!Files.exists(filePath)) {
	        return ResponseEntity.notFound().build();
	    }

	    org.springframework.core.io.Resource resource = new FileSystemResource(filePath.toFile());

	    // Determine content type
	    String contentType = Files.probeContentType(filePath);
	    if (contentType == null) {
	        contentType = "application/octet-stream";
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
	            .body(resource);
	}


	@PostMapping
	public String insertUpdateTasks(@RequestParam String opration , 
			@RequestParam(defaultValue = "0") Long titleId, 
			@RequestParam(required = true)  String title, 
			@RequestParam(required = false) String desc, 
			@RequestParam(required = true)  String priority, 
			@RequestParam(required = false)  String dueDt, 
			@RequestParam(required = false)  MultipartFile taskAttachment, 
			@RequestParam(required = false)  String email, 
			@RequestParam(defaultValue = "0", name = "category_cd") Long categoryCd,
			@RequestParam(required = false) String allCompleted, 
			@RequestParam(defaultValue = "0") Integer filt_category_cd,
			@RequestParam(required = false) String from_dt,
			@RequestParam(required = false) String to_dt, 
			RedirectAttributes redirectAttributes) throws Exception
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);  // To strictly parse the date
        Date due_date = new Date();
        
        if(!dueDt.equals("")) {
        	due_date = formatter.parse(dueDt);
        }
		
		Boolean isAttached = false;
		
		String uniqueFilename ="";
		
		if(taskAttachment != null && !taskAttachment.getOriginalFilename().equals("")) //Here make sure to use getOriginalFilename not just getName, getName will always return the input type name
		{
			 // 1. Validate file size
		    long maxSizeInBytes = 5 * 1024 * 1024; // 5MB
		    if (taskAttachment.getSize() > maxSizeInBytes)
		    {
		        redirectAttributes.addFlashAttribute("message", "Error - File size exceeds 5MB limit.");
		        return "redirect:/task_mgmt/tasks";
		    }

		    // 2. Validate file extension (only allow certain types)
		    String originalFilename = taskAttachment.getOriginalFilename();
		    String extension = "";

		    if (originalFilename != null && originalFilename.contains(".")) {
		        extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
		    }

		    List<String> allowedExtensions = List.of("pdf", "jpg", "jpeg", "png", "doc", "docx", "xls", "xlsx");

		    if (!allowedExtensions.contains(extension)) 
		    {
		        redirectAttributes.addFlashAttribute("message", "Error - Invalid file type. Allowed: pdf, jpg, jpeg, png, doc, docx, xls, xlsx");
		        return "redirect:/task_mgmt/tasks";
		    }

		    // 3. Sanitize filename to avoid path traversal (remove suspicious characters)
		    String sanitizedFilename = originalFilename.replaceAll("[\\\\/:*?\"<>|]", "_");

		    
		    int dotIndex = sanitizedFilename.lastIndexOf('.');
		    String namePart = (dotIndex == -1) ? sanitizedFilename : sanitizedFilename.substring(0, dotIndex);
		    extension = (dotIndex == -1) ? "" : sanitizedFilename.substring(dotIndex + 1);
		    
		    // Optional: Add time-stamp to prevent overwriting
		    uniqueFilename =namePart + "_" + System.currentTimeMillis()+"."+extension ;
		    
		    String projectRootPath = new File(".").getCanonicalPath();
		    
			String uploadDir = projectRootPath + File.separator +commonVariable.taskFileDir; 
			File uploadFolder = new File(uploadDir);
			
			if(!uploadFolder.exists()) 
			{
				uploadFolder.mkdirs();
			}
			
			//String originalFilename = taskAttachment.getOriginalFilename();
		    String filePath = uploadDir + File.separator + uniqueFilename;
		    
		    taskAttachment.transferTo(new File(filePath));
			
			isAttached = true;
		}
		
		if(opration.equals("INSERT")) 
		{
			TaskCategory category = taskService.getCategoryById(categoryCd);
			LocalDateTime ent_dt = LocalDateTime.now();
			
			taskService.createTask(title,desc, category,ent_dt, priority, due_date, isAttached, email);
			redirectAttributes.addFlashAttribute("message", title+" added successfully.");
		}
		else 
		{
			LocalDateTime mod_dt = LocalDateTime.now();
			TaskCategory category = taskService.getCategoryById(categoryCd);
			
			taskService.updatetaskTitle(titleId, title, desc,category, mod_dt, priority, due_date, isAttached, email);
			redirectAttributes.addFlashAttribute("message", title+" updated successfully!");
		}
		
		if (isAttached) 
		{
		    taskService.addTaskFileDtls(titleId, uniqueFilename); // Call service with Task
		}
		
		return "redirect:/task_mgmt/tasks?allCompleted="+allCompleted+"&filt_category_cd="+filt_category_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;
	}
	
	@PreAuthorize("hasAuthority('TASK_DELETE')")
	@GetMapping("/{id}/delete")
	public String deleteTasks(@PathVariable Long id) {

		taskService.deleteTask(id);
		return "redirect:/task_mgmt/tasks";
	}

	@GetMapping("/{id}/toggle")
	public String toggleTasks(@PathVariable Long id) {

		taskService.toggleTask(id);
		return "redirect:/task_mgmt/tasks";
	}
	
	
	@PostMapping("/import-file")
	public String importFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) 
	{
		try {
			
			if(!file.isEmpty()) 
			{
				// Process the Excel file
				InputStream inputStream = file.getInputStream();
				
				 Workbook workbook = new XSSFWorkbook(inputStream);		
				 Sheet sheet = workbook.getSheetAt(0);
				 Iterator<Row> rowIterator = sheet.iterator();
				 
				 // Process each row (assuming the first column contains task titles)
				 while (rowIterator.hasNext()) 
				 {
					 Row row = rowIterator.next();
					 Cell cell1 = row.getCell(0); // Assuming the first column is the task title
					
					 if (cell1 != null) 
					 {
						 String title = cell1.getStringCellValue();
						 if(!title.isEmpty())
						 {
							 taskService.importTask(title);
						 }
					 }
				 }
				 workbook.close();
			}
			else
			{
				redirectAttributes.addFlashAttribute("message", "Imported File is Empty.");
				return  "redirect:/task_mgmt/tasks";
			}
			
			redirectAttributes.addFlashAttribute("message", "File imported successfully.");
		} 
		catch (Exception e) 
		{
			redirectAttributes.addFlashAttribute("message", "Error while importing file.");
			e.printStackTrace();
		}
		
		return  "redirect:/task_mgmt/tasks";
	}
	
	@GetMapping("/task_mgmt/frm_tasks_category_mst")
	public String getTaskCategoryMst(Model model) 
	{
		List<TaskCategory> taskCategory = taskService.getAlltasksCategory();
		
		model.addAttribute("task_categories", taskCategory);
		model.addAttribute("task_categories_size", taskCategory.size());
		
		return "/task_mgmt/frm_tasks_category_mst";
	}
	
	@PostMapping("/task_mgmt/frm_tasks_category_mst")
	public String InsertUpdateTaskCategory(RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Long category_cd, @RequestParam String opration, @RequestParam String category_nm,@RequestParam String category_abbr,@RequestParam String status_flag) 
	{
		LocalDateTime ent_dt = LocalDateTime.now();
		LocalDateTime mod_dt = LocalDateTime.now();
		
		if(opration.equals("MODIFY")) 
		{
			taskService.updateTaskCategory(redirectAttributes, category_cd, category_nm, category_abbr, status_flag, mod_dt);
			
			redirectAttributes.addFlashAttribute("message", "Successfully - "+category_nm+" Updated To Category Master!");
		}
		else
		{
			taskService.createTaskCategory(category_nm, category_abbr, status_flag, ent_dt);
			
			redirectAttributes.addFlashAttribute("message", "Successfully - "+category_nm+" Added To Category Master!");
		}
		
		return "redirect:/task_mgmt/frm_tasks_category_mst";
	}
	
}
