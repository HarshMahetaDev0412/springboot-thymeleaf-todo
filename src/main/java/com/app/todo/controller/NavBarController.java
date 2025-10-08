package com.app.todo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.app.todo.dto.ModuleDTO;
import com.app.todo.dto.SubMenuDTO;
import com.app.todo.models.Form;
import com.app.todo.models.ModuleEntity;
import com.app.todo.models.User;
import com.app.todo.services.FormService;
import com.app.todo.services.ModuleService;
import com.app.todo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class NavBarController {

    private final ModuleService moduleService;
    private final FormService formService;
    private final UserService userService;

    public NavBarController(ModuleService moduleService, FormService formService, UserService userService) {
        this.moduleService = moduleService;
        this.formService = formService;
        this.userService = userService;
    }
    
    @ModelAttribute
    public void addGlobalModules(Model model, Principal principal, HttpServletRequest request) {
        
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
    	if (principal == null)
    	{
    		return;
    	}
    	 
    	// Log the request URI to identify which page is calling this
        String requestURI = request.getRequestURI();
    	
    	List<ModuleEntity> modules = moduleService.getAllModulesByFlagOrderByPriority();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        
        for (ModuleEntity module : modules) {
            Long moduleCd = module.getModule_cd();

            List<Form> forms = formService.getFormsByModuleCdAndFlagTrue(moduleCd);

            Map<Long, SubMenuDTO> subMenuMap = new LinkedHashMap<>();

            for (Form form : forms) {
                Long subMenuCd = form.getSub_menu_cd();
                if (!subMenuMap.containsKey(subMenuCd)) {
                    SubMenuDTO subMenu = new SubMenuDTO();
                    subMenu.setSubMenuCd(subMenuCd);
                    subMenu.setSubMenuNm(form.getSub_menu_nm());
                    subMenuMap.put(subMenuCd, subMenu);
                }
                subMenuMap.get(subMenuCd).getForms().add(form);
            }

            ModuleDTO dto = new ModuleDTO();
            dto.setModule(module);
            dto.setSubMenus(new ArrayList<>(subMenuMap.values()));
            moduleDTOs.add(dto);
        }

        model.addAttribute("global_moduleDTOs", moduleDTOs);
        
        if (principal != null) 
        {
            String username = principal.getName(); 
            User user = userService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + username));
            
            model.addAttribute("loggeduser", user);
        }
    }
}
