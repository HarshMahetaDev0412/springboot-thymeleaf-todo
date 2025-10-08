package com.app.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.todo.models.Form;

@Repository
public interface FormReporitory extends JpaRepository<Form, Long>
{
	List<Form> findAllByModuleCd(Long moduleCd);

	
	@Query("SELECT DISTINCT f.sub_menu_cd, f.sub_menu_nm FROM Form f WHERE f.moduleCd = ?1")
	List<Object[]> findDistinctSubMenus(Long moduleCd);


	@Query("SELECT MAX(f.sub_menu_cd) FROM Form f WHERE f.moduleCd = ?1")
	Long findMaxGroupCd(Long moduleCd);
	
	List<Form> findByModuleCdAndFlagTrueOrderBySubMenuSeqAscFormNmAsc(Long moduleCd);


	@Query("""
		    SELECT DISTINCT f 
		    FROM Form f
		    WHERE f NOT IN (SELECT ar.form FROM AccessRight ar)
		    OR f IN (
		        SELECT ar.form
		        FROM AccessRight ar
		        WHERE ar.hasAccess = false
		        AND ar.form NOT IN (
		            SELECT ar2.form 
		            FROM AccessRight ar2 
		            WHERE ar2.hasAccess = true
		        )
		    )
		""")
		List<Form> findFormsNotAccessibleOrDenied();


}
