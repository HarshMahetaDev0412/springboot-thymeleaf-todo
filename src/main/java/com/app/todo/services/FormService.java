package com.app.todo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.todo.models.Form;
import com.app.todo.models.Permission;
import com.app.todo.repository.FormReporitory;
import com.app.todo.repository.PermissionRepository;

@Service
public class FormService 
{
	@Autowired
	private final FormReporitory formReporitory;
	private final PermissionRepository permissionRepository;
	
	public FormService(FormReporitory formReporitory, PermissionRepository permissionRepository) 
	{
		this.formReporitory = formReporitory;
		this.permissionRepository = permissionRepository;
	}

	public List<Form> getFormsForModule(Long module_cd) 
	{
		return formReporitory.findAllByModuleCd(module_cd);
	}

	public Form getFormDetails(Long menu_cd) {

		return formReporitory.findById(menu_cd).orElse(null);
	}

	public List<Object[]> getSubMenuForModule(Long module_cd) {
		
		return formReporitory.findDistinctSubMenus(module_cd);
	}

	public void insertMenu(Long module_cd, String menu_nm, Long group_cd, String grpNm, String new_grp_nm,
			String menu_path, String menu_type, boolean flag) 
	{
		Form form = new Form();
		
		form.setModuleCd(module_cd);
		form.setFormNm(menu_nm);
		form.setSub_menu_cd(group_cd);
		if(new_grp_nm.equals(""))
		{
			form.setSub_menu_nm(grpNm);
		}
		else
		{
			form.setSub_menu_nm(new_grp_nm);
		}
		form.setPath(menu_path);
		form.setType(menu_type);
		form.setFlag(flag);
		
		formReporitory.save(form);
	}

	public void updateMenu(Long module_cd, Long menu_cd, String menu_nm, Long group_cd, String grpNm, String new_grp_nm,
			String menu_path, String menu_type, boolean flag) {

		Form form = formReporitory.findById(menu_cd).orElse(null);
		
		if(form != null)
		{
			form.setModuleCd(module_cd);
			form.setFormNm(menu_nm);
			form.setSub_menu_cd(group_cd);
			if(new_grp_nm.equals(""))
			{
				form.setSub_menu_nm(grpNm);
			}
			else
			{
				form.setSub_menu_nm(new_grp_nm);
			}
			form.setPath(menu_path);
			form.setType(menu_type);
			form.setFlag(flag);

			formReporitory.save(form);
		}
		else
		{
			System.out.println("No Form Found");
		}
	}

	public Long findMaxGroupCd(Long moduleCd) 
	{
		Long maxGroupCd = formReporitory.findMaxGroupCd(moduleCd);
		return maxGroupCd;
	}

	public List<Form> getAllForms() {
		return formReporitory.findAll();
	}
	
	public List<Form> getFormsByModuleCdAndFlagTrue(Long moduleCd) {
        return formReporitory.findByModuleCdAndFlagTrueOrderBySubMenuSeqAscFormNmAsc(moduleCd);
    }

	public List<Permission> getAllPermissions() {

		return permissionRepository.findAll();
	}

	public List<Form> findFormNotAccessed() 
	{
		
		return formReporitory.findFormsNotAccessibleOrDenied();
	}
}
