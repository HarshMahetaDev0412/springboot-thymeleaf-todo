package com.app.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.todo.models.ModuleEntity;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long>
{
	List<ModuleEntity> findAllByFlagTrueOrderByPriority();
}
