package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageCarouselAction;

@Repository
public interface MessageCarouselActionRepository extends JpaRepository<MessageCarouselAction, Long> {

	@Query(value = "select distinct M from MessageCarouselAction M " 
			     + "where M.columnId = :templateId " 
			     + "and M.templateType = :templateType")
	public List<MessageCarouselAction> findByTemplateIdAndTemplateType(@Param("templateId") Integer templateId, @Param("templateType") String templateType);

}
