package com.app.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.todo.models.ModuleEntity;
import com.app.todo.services.FormService;
import com.app.todo.services.ModuleService;

@Controller
public class ModuleController 
{
	@Autowired
	private final ModuleService moduleService;
	private final FormService formService;
	
	public ModuleController(ModuleService moduleService, FormService formService) {
		this.moduleService = moduleService;
		this.formService = formService;
	}
	
	@GetMapping("/admin/frm_add_modify_module")
	public String getAlInserUpdateModule(Model model) 
	{
		List<ModuleEntity> module = moduleService.getAllModules();
		
		model.addAttribute("module", module);
		model.addAttribute("module_size", module.size());
		
		return "/admin/frm_add_modify_module";
	}
	
	@PostMapping("/admin/frm_add_modify_module")
	public String InserUpdateModule(@RequestParam(required = true) Long[] module_cd ,@RequestParam String[] module_nm, @RequestParam Integer[] module_priority, @RequestParam String[] active_flag)
	{
		
		if(module_cd.length>0) 
		{
			for(int i=0; i<module_cd.length;i++) 
			{
				boolean flag=false;
				
				if(active_flag[i].equals("Y")) {
					
					flag = true;
				}
				
				moduleService.insertUpdateModule(module_cd[i],module_nm[i],module_priority[i],flag);
			}
		}
		return "redirect:/admin/frm_add_modify_module";
	}
	
}
