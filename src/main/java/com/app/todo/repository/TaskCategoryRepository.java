package com.app.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.todo.models.TaskCategory;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long>
{

	List<TaskCategory> findAllByStatus(String string);
}
