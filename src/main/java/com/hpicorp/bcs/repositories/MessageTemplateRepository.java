package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.MessageTemplate;

@Repository
public interface MessageTemplateRepository extends PagingAndSortingRepository<MessageTemplate, Integer> {

	@Modifying
	@Transactional
	@Query(value = "delete from message_template_action where template_id = :templateId ", nativeQuery = true)
	public void deleteByTemplateID(@Param("templateId") long templateId);

	@Query(value = "select M from MessageTemplate M order by M.id desc ")
	public List<MessageTemplate> getMessageTeamplateByType();
	
	@Query(value = "select M from MessageTemplate M where M.type = 'buttons' or M.type = 'confirm' order by M.id desc ")
	public Page<MessageTemplate> getMessageTeamplateByType(Pageable pageable);
	
}
