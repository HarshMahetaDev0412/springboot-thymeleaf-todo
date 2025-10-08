package com.app.todo.config;

import com.app.todo.models.Permission;
import com.app.todo.models.Role;
import com.app.todo.repository.PermissionRepository;
import com.app.todo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    public DataInitializer(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
       
    	// ===== TASK/TODO PERMISSIONS =====
        Permission READ = createPermissionIfNotExists("READ", "View todo forms");
        Permission WRITE = createPermissionIfNotExists("WRITE", "Write/Update/Delete in todo forms");
        Permission CHECK = createPermissionIfNotExists("CHECK", "Check access");
        Permission PRINT = createPermissionIfNotExists("PRINT", "Print access");
        Permission DELETE = createPermissionIfNotExists("DELETE", "Delete access");
        Permission AUDIT = createPermissionIfNotExists("AUDIT", "Audit access");
        Permission AUTHORIZE = createPermissionIfNotExists("AUTHORIZE", "Autherize access");
        Permission APPROVE = createPermissionIfNotExists("APPROVE", "Approve access");
        Permission EXECUTE = createPermissionIfNotExists("EXECUTE", "Execute access");
        
        // ===== SYSTEM PERMISSIONS =====
        Permission SYSTEM_SETTINGS = createPermissionIfNotExists("SYSTEM_SETTINGS", "Modify system settings");
        Permission AUDIT_LOGS = createPermissionIfNotExists("AUDIT_LOGS", "View audit logs");
        
        // ===== ROLE DEFINITIONS =====
        // Regular User - Basic task operations
        createRoleIfNotExists("USER", "Regular User", Arrays.asList(
            READ, 
            WRITE
        ));
        
        // Manager - Extended task operations + user viewing
        createRoleIfNotExists("MANAGER", "Manager", Arrays.asList(
        		READ, WRITE, PRINT, CHECK, DELETE, APPROVE, EXECUTE
        ));
        
        // Admin - Full system access
        createRoleIfNotExists("ADMIN", "Administrator", Arrays.asList(
        		READ, WRITE, PRINT, CHECK,DELETE,AUDIT,APPROVE,EXECUTE,AUTHORIZE,
            SYSTEM_SETTINGS, AUDIT_LOGS
        ));
        
        // Super Admin - Everything (could be separate if needed)
        createRoleIfNotExists("SUPER_ADMIN", "Super Administrator", Arrays.asList(
        		READ, WRITE, PRINT, CHECK,DELETE,AUDIT,APPROVE,EXECUTE,AUTHORIZE,
                SYSTEM_SETTINGS, AUDIT_LOGS
        ));
    }
    
    private Permission createPermissionIfNotExists(String name, String description) {
        return permissionRepository.findByName(name)
            .orElseGet(() -> permissionRepository.save(new Permission(name, description)));
    }
    
    private Role createRoleIfNotExists(String name, String description, java.util.List<Permission> permissions) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
            	System.out.println("Role Created : "+ name);
                Role role = new Role(name, description);
                role.setPermissions(new HashSet<>(permissions));
                return roleRepository.save(role);
            });
    }
}