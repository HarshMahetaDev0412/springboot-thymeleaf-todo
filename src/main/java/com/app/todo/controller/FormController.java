package com.app.todo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.todo.AccessRightsUpdatedEvent;
import com.app.todo.models.AccessRight;
import com.app.todo.models.Form;
import com.app.todo.models.ModuleEntity;
import com.app.todo.models.Permission;
import com.app.todo.models.Role;
import com.app.todo.models.User;
import com.app.todo.repository.AccessRightRepository;
import com.app.todo.repository.PermissionRepository;
import com.app.todo.services.FormService;
import com.app.todo.services.ModuleService;
import com.app.todo.services.UserService;

@Controller
public class FormController 
{

    private final PermissionRepository permissionRepository;
	@Autowired
	private final FormService formService;
	@Autowired
	private final ModuleService moduleService;
	private final UserService userService;
	private final AccessRightRepository accessRightRepository;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public FormController(FormService formService, ModuleService moduleService, UserService userService, PermissionRepository permissionRepository,
			AccessRightRepository accessRightRepository) 
	{
		this.formService = formService;
		this.moduleService = moduleService;
		this.userService = userService;
		this.permissionRepository = permissionRepository;
		this.accessRightRepository = accessRightRepository;
	}
	
	@GetMapping("/admin/frm_add_modify_menu")
	public String getAddModifyMenuDtl(Model model, @RequestParam(name="module_cd", defaultValue="0") Long module_cd, @RequestParam(name="menu_cd", defaultValue="0") Long menu_cd) 
	{
		List<ModuleEntity> modules = moduleService.getAllModulesByFlagOrderByPriority();
        List<Form> form = formService.getFormsForModule(module_cd);
        List<Object[]> subMenus = formService.getSubMenuForModule(module_cd);
        
        Form formDetails = formService.getFormDetails(menu_cd);
        
        model.addAttribute("global_module", modules);
        model.addAttribute("form", form);
        model.addAttribute("subMenu", subMenus);
        model.addAttribute("formDetails", formDetails);
      
        model.addAttribute("module_cd", module_cd);
        model.addAttribute("menu_cd", menu_cd);
		
		return "/admin/frm_add_modify_menu";
	}
	
	@PostMapping("/admin/frm_add_modify_menu")
	public String InsertUpdadateMenu(@RequestParam Long module_cd,@RequestParam Long menu_cd, 
			@RequestParam String menu_nm, @RequestParam String group_cd, @RequestParam String grpNm, @RequestParam String new_grp_nm,
			@RequestParam String menu_path, @RequestParam String menu_type, @RequestParam String status_flag) 
	{

		boolean flag = false;
		Long maxGroupCd = formService.findMaxGroupCd(module_cd);
		
		if(status_flag.equals("Y")) {
			flag = true;
		}
		
		if(group_cd.equals("other"))
		{
			if (maxGroupCd == null) 
			{
			    maxGroupCd = 1L; // Start from 1 if no values exist
			} else {
			    maxGroupCd = maxGroupCd + 1;
			}
		}
		
		System.out.println("Post method menu_cd----"+menu_cd);
		
		if(menu_cd == 0 || menu_cd.equals(null))
		{
			formService.insertMenu(module_cd, menu_nm,  maxGroupCd,  grpNm,  new_grp_nm, menu_path,  menu_type, flag);
		}
		else
		{
			formService.updateMenu(module_cd, menu_cd, menu_nm,  maxGroupCd,  grpNm,  new_grp_nm, menu_path,  menu_type, flag);
		}
		
		
		return "redirect:/admin/frm_add_modify_menu?module_cd="+module_cd+"&menu_cd="+menu_cd; 
	}
	
	@GetMapping("/admin/frm_add_modify_access_right")
	public String getAccessRights(Model model, @RequestParam(defaultValue = "0") String module_cd, @RequestParam(defaultValue = "0") String group_cd)
	{
		List<Role> accessGroup = userService.getAllRoles();
		List<ModuleEntity> modules = moduleService.getAllModulesByFlagOrderByPriority();
		List<Form> forms = formService.getFormsForModule(Long.valueOf(module_cd));
		
		List<Permission> permissions = formService.getAllPermissions().stream()
				.filter(permission -> !permission.getName().equals("AUDIT_LOGS") && !permission.getName().equals("SYSTEM_SETTINGS"))
				.collect(Collectors.toList());
		
		Optional<Role> role = userService.getRoleById(Long.valueOf(group_cd));
		
		List<Boolean> VREAD_ACS_FLAG = new ArrayList<>();
		List<Boolean> VWRITE_ACS_FLAG = new ArrayList<>();
		List<Boolean> VCHECK_ACS_FLAG = new ArrayList<>();
		List<Boolean> VPRINT_ACS_FLAG = new ArrayList<>();
		List<Boolean> VDELETE_ACS_FLAG = new ArrayList<>();
		List<Boolean> VAUDIT_ACS_FLAG = new ArrayList<>();
		List<Boolean> VAUTHORIZE_ACS_FLAG = new ArrayList<>();
		List<Boolean> VAPPROVE_ACS_FLAG = new ArrayList<>();
		List<Boolean> VEXECUTE_ACS_FLAG = new ArrayList<>();
		
		for(Form form : forms)
		{
			// Read Access
	        Optional<Permission> readPermission = permissionRepository.findByName("READ");
	        AccessRight readAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, readPermission.orElse(null));
	        VREAD_ACS_FLAG.add(readAccessRight != null ? readAccessRight.getHasAccess() : false);

	        // Write Access
	        Optional<Permission> writePermission = permissionRepository.findByName("WRITE");
	        AccessRight writeAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, writePermission.orElse(null));
	        VWRITE_ACS_FLAG.add(writeAccessRight != null ? writeAccessRight.getHasAccess() : false);

	        // Check Access
	        Optional<Permission> checkPermission = permissionRepository.findByName("CHECK");
	        AccessRight checkAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, checkPermission.orElse(null));
	        VCHECK_ACS_FLAG.add(checkAccessRight != null ? checkAccessRight.getHasAccess() : false);

	        // Print Access
	        Optional<Permission> printPermission = permissionRepository.findByName("PRINT");
	        AccessRight printAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, printPermission.orElse(null));
	        VPRINT_ACS_FLAG.add(printAccessRight != null ? printAccessRight.getHasAccess() : false);

	        // Delete Access
	        Optional<Permission> deletePermission = permissionRepository.findByName("DELETE");
	        AccessRight deleteAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, deletePermission.orElse(null));
	        VDELETE_ACS_FLAG.add(deleteAccessRight != null ? deleteAccessRight.getHasAccess() : false);

	        // Audit Access
	        Optional<Permission> auditPermission = permissionRepository.findByName("AUDIT");
	        AccessRight auditAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, auditPermission.orElse(null));
	        VAUDIT_ACS_FLAG.add(auditAccessRight != null ? auditAccessRight.getHasAccess() : false);

	        // Authorize Access
	        Optional<Permission> authorizePermission = permissionRepository.findByName("AUTHORIZE");
	        AccessRight authorizeAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, authorizePermission.orElse(null));
	        VAUTHORIZE_ACS_FLAG.add(authorizeAccessRight != null ? authorizeAccessRight.getHasAccess() : false);

	        // Approve Access
	        Optional<Permission> approvePermission = permissionRepository.findByName("APPROVE");
	        AccessRight approveAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, approvePermission.orElse(null));
	        VAPPROVE_ACS_FLAG.add(approveAccessRight != null ? approveAccessRight.getHasAccess() : false);

	        // Execute Access
	        Optional<Permission> executePermission = permissionRepository.findByName("EXECUTE");
	        AccessRight executeAccessRight = accessRightRepository.findByRoleAndFormAndPermission(role.get(), form, executePermission.orElse(null));
	        VEXECUTE_ACS_FLAG.add(executeAccessRight != null ? executeAccessRight.getHasAccess() : false);
	    }
		
		model.addAttribute("accessGroup", accessGroup);
		model.addAttribute("modules", modules);
		model.addAttribute("module_cd", module_cd);
		model.addAttribute("group_cd", group_cd);
		model.addAttribute("forms", forms);
		model.addAttribute("permissions", permissions);
		model.addAttribute("VREAD_ACS_FLAG", VREAD_ACS_FLAG);
		model.addAttribute("VWRITE_ACS_FLAG", VWRITE_ACS_FLAG);
		model.addAttribute("VPRINT_ACS_FLAG", VPRINT_ACS_FLAG);
		model.addAttribute("VCHECK_ACS_FLAG", VCHECK_ACS_FLAG);
		model.addAttribute("VDELETE_ACS_FLAG", VDELETE_ACS_FLAG);
		model.addAttribute("VAUDIT_ACS_FLAG", VAUDIT_ACS_FLAG);
		model.addAttribute("VAUTHORIZE_ACS_FLAG", VAUTHORIZE_ACS_FLAG);
		model.addAttribute("VAPPROVE_ACS_FLAG", VAPPROVE_ACS_FLAG);
		model.addAttribute("VEXECUTE_ACS_FLAG", VEXECUTE_ACS_FLAG);
		
		return "/admin/frm_add_modify_access_right";
	}
	
	@PostMapping("/admin/frm_add_modify_access_right")
	public String InInsertUpdateAccessRights(@RequestParam(defaultValue = "0") String module_cd, @RequestParam(defaultValue = "0") String group_cd,
			@RequestParam(defaultValue = "") String[] READ_acs_flag,
			@RequestParam(defaultValue = "") String[] WRITE_acs_flag,
			@RequestParam(defaultValue = "") String[] CHECK_acs_flag,
			@RequestParam(defaultValue = "") String[] PRINT_acs_flag,
			@RequestParam(defaultValue = "") String[] DELETE_acs_flag,
			@RequestParam(defaultValue = "") String[] AUDIT_acs_flag,
			@RequestParam(defaultValue = "") String[] AUTHORIZE_acs_flag,
			@RequestParam(defaultValue = "") String[] APPROVE_acs_flag,
			@RequestParam(defaultValue = "") String[] EXECUTE_acs_flag,
			@RequestParam(defaultValue = "") String[] menu_cd
			) 
	{

		// Process the access rights form
		int a=0;
		
	    for (String menuCode : menu_cd) {
	        // Retrieve the menu (form) by its code
	        Optional<Form> menuOpt = Optional.ofNullable(formService.getFormDetails(Long.valueOf(menuCode)));
	        
	        if (menuOpt.isPresent())
	        {
	        	Form menu = menuOpt.get();
	            
	            // Loop through permissions and set the access rights for each permission

	        	for (Permission permission : permissionRepository.findAll()) 
	            {
	                Boolean hasAccess = false;

	                if(permission.getName().equals("READ") && READ_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("WRITE") && WRITE_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("CHECK") && CHECK_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("PRINT") && PRINT_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("DELETE") && DELETE_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("AUDIT") && AUDIT_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("AUTHORIZE") && AUTHORIZE_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("APPROVE") && APPROVE_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                if(permission.getName().equals("EXECUTE") && EXECUTE_acs_flag[a].equals("Y")) 
	                {
	                	hasAccess = true;
	                }
	                
	                Optional<Role> role = userService.getRoleById(Long.valueOf(group_cd)); 
	                
	                // Save the access rights
	                AccessRight accessRight = accessRightRepository
	                        .findByRoleAndFormAndPermission(role.get() ,menu, permission);
	                if (accessRight != null)
	                {
	                    accessRight.setHasAccess(hasAccess);
	                    accessRightRepository.save(accessRight);
	                }
	                else
	                {
	                	AccessRight newAccessRight = new AccessRight();
	                	
	                	newAccessRight.setForm(menu);
	                	newAccessRight.setRole(role.get());
	                	newAccessRight.setPermission(permission);
	                	newAccessRight.setHasAccess(hasAccess);
	                	accessRightRepository.save(newAccessRight);
	                }
	            }
	        }
	        
	        a++;
	    }
	    
	    System.out.println("FormContoller : Event Published!!");
	    eventPublisher.publishEvent(new AccessRightsUpdatedEvent(this));
	    
		return "redirect:/admin/frm_add_modify_access_right?module_cd="+module_cd+"&group_cd="+group_cd;
	}
}
