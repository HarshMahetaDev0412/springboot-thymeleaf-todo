package com.app.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.todo.models.AccessRight;
import com.app.todo.models.Form;
import com.app.todo.models.Permission;
import com.app.todo.models.Role;

@Repository
public interface AccessRightRepository extends JpaRepository<AccessRight, Long>
{
	List<AccessRight> findByRole(Role role);
    List<AccessRight> findByForm(Form form);
	AccessRight findByRoleAndFormAndPermission(Role group_cd, Form menu, Permission permission);
	
	List<AccessRight> findByRoleAndHasAccessTrue(Role role);
	
	List<AccessRight> findDistinctByHasAccessFalse();
}
