package com.app.todo.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.app.todo.models.Task;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>
{
	public List<Task> findAllByCompletedOrderByTitleAsc(boolean completed);
	
	public List<Task> findAllByOrderByTitleAsc();
	
	/*
	 * Rules for Naming Custom Finder Methods
	 * 
	 * Start with a keyword
	 * 
	 * Common ones:
	 * 
	 * findBy...
	 * 
	 * readBy...
	 * 
	 * getBy...
	 * 
	 * queryBy... 👉 Example: findByCategory(String category)
	 * 
	 * Use Entity Field Names
	 * 
	 * After the keyword, use the exact property names of your Entity
	 * (case-insensitive but must match field names).
	 *  👉 Example: Entity has field
	 * date → findByDate(...) works. 
	 * 👉 Wrong: findByCreatedDate(...) (if field is
	 * just date).
	 * 
	 * Add Operators (if needed)
	 * 
	 * Some common operators:
	 * 
	 * Between → findByDateBetween(LocalDate start, LocalDate end)
	 * 
	 * GreaterThan / LessThan → findByAmountGreaterThan(Double amount)
	 * 
	 * Like → findByDescriptionLike(String keyword)
	 * 
	 * Containing → findByDescriptionContaining(String word) (similar to %LIKE%)
	 * 
	 * OrderBy...Asc/Desc → findByCategoryOrderByAmountDesc()
	 * 
	 * Combine with AND / OR
	 * 
	 * You can chain conditions:
	 * 
	 * findByCategoryAndDate(String category, LocalDate date)
	 * 
	 * findByAmountGreaterThanAndCategory(Double amount, String category)
	 * 
	 * findByCategoryOrDescription(String category, String desc)
	 * 
	 * Return Types
	 * 
	 * Can return List<Expense>, Optional<Expense>, or even Page<Expense> (for
	 * pagination).
	 */
	
}
