package com.app.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.todo.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role>
{
	Optional<Role> findByName(String string);
	boolean existsByName(String name);
	
	@Query("SELECT r FROM Role r JOIN r.users u WHERE u.email = :email")
	List<Role> findByEmailWithRoles(@Param("email") String email);

}
