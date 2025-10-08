package com.app.todo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.todo.models.ModuleEntity;
import com.app.todo.repository.ModuleRepository;

@Service
public class ModuleService {
	
	private final ModuleRepository moduleRepository;
	
	public ModuleService(ModuleRepository moduleRepository)
	{
		this.moduleRepository = moduleRepository;
	}

	public List<ModuleEntity> getAllModules()
	{
		return moduleRepository.findAll();
	}

	public List<ModuleEntity> getAllModulesByFlagOrderByPriority()
	{
		return moduleRepository.findAllByFlagTrueOrderByPriority();
	}

	public void insertUpdateModule(Long module_cd, String module_nm, Integer module_priority, Boolean active_flag) {
		
		ModuleEntity module = new ModuleEntity();
		
		module.setModule_cd(module_cd);
		module.setModule_name(module_nm);
		module.setPriority(module_priority);
		module.setFlag(active_flag);
		
		moduleRepository.save(module);
		
	}
}
