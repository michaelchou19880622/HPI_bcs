package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageTemplateAction;

@Repository
public interface MessageTemplateActionRepository extends JpaRepository<MessageTemplateAction, Long> {

	@Query(value = "select distinct M from MessageTemplateAction M " 
				 + "where M.templateId = :templateId "
				 + "and M.templateType = :templateType")
	public List<MessageTemplateAction> findByTemplateIdAndTemplateType(@Param("templateId") Integer templateId, @Param("templateType") String templateType);

}
