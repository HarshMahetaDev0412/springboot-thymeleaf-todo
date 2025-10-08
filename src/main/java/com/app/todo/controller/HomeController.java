package com.app.todo.controller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.todo.models.Task;
import com.app.todo.services.HomeExportService;
import com.app.todo.services.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Controller
public class HomeController 
{
	@Autowired
	private final HomeExportService homeExportService;
	private final TaskService taskService;
	
	public HomeController(HomeExportService homeExportService, TaskService taskService) 
	{
		this.homeExportService = homeExportService;
		this.taskService = taskService;
	}
	
	@GetMapping("/home/todo_home")
	public String getHomePageDetails(Model model, Principal principal) throws JsonProcessingException
	{
		List<Task> tasks = taskService.getAlltasks();
		List<Task> taskCompleted = taskService.getCompletedTasks(false);
		List<Task> taskPending= taskService.getCompletedTasks(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")); //useisng a format that matches TimeZone
		objectMapper.registerModule(new JavaTimeModule()); // Registers Java 8 date/time support
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Disable writing dates as timestamps 
		//?UP : To handle this Dependency added look pom.xml if needed

		String tasksJson = objectMapper.writeValueAsString(tasks);
		
		model.addAttribute("tasksJson",tasksJson);
		model.addAttribute("totalTasks",tasks.size());
		model.addAttribute("pendingTasks",taskCompleted.size());
		model.addAttribute("completedTasks",taskPending.size());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    System.out.println("Authenticated user: " + auth.getName());
	    System.out.println("Authorities: ");
	    auth.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority())); // Should print "ROLE_USER", etc.
	    
		
		return "home/todo_home";
	}
	
	@GetMapping("/todo_home/export/excel")
	public ResponseEntity<InputStreamResource> exportToXSLX(@RequestParam(name="allCompleted",required = false) String allCompleted, 
			@RequestParam(name="filt_category_cd",required = false) Integer filt_category_cd,
			@RequestParam(name="from_dt",required = false) String from_dt,
			@RequestParam(name="to_dt",required = false) String to_dt) throws Exception
	{
		ByteArrayInputStream stream = homeExportService.exportToXLSX(filt_category_cd, allCompleted,from_dt,to_dt);
		
		// Wrapping the ByteArrayInputStream in an InputStreamResource
	    InputStreamResource resource = new InputStreamResource(stream);

	    // Returning the resource with proper headers
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=tasks.xlsx")
	            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	            .body(resource);
	}
	
//	@GetMapping("/todo_home/export/pdf")
//    public ResponseEntity<InputStreamResource> exportPDF() {
//        var stream = homeExportService.exportToPDF();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=tasks.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(new InputStreamResource(stream));
//    }

    @GetMapping("/todo_home/export/docx")
    public ResponseEntity<InputStreamResource> exportDOCX() throws Exception {
        var stream = homeExportService.exportToDOCX();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=tasks.docx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/home/globalError")
    public String errorHandler() {

    	return "home/globalError";
    }
}
