package com.app.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.todo.models.TaskFileDtl;
import com.app.todo.models.TaskFileDtlId;

@Repository
@EnableJpaRepositories
public interface TaskFileDtlRepository extends JpaRepository<TaskFileDtl, TaskFileDtlId> , JpaSpecificationExecutor<TaskFileDtl>
{
	@Query("SELECT COALESCE(MAX(t.fileSeq), 0) FROM TaskFileDtl t WHERE t.task.id = :taskId")
	int findMaxFileSeqByTaskId(@Param("taskId") Long taskId);
	
	@Query("SELECT t FROM TaskFileDtl t WHERE t.task.id = :taskId AND t.fileSeq = (" +
			"SELECT MAX(t2.fileSeq) FROM TaskFileDtl t2 WHERE t2.task.id = t.task.id)")
	Optional<TaskFileDtl> findLatestFileByTaskId(@Param("taskId") Long taskId);

}
