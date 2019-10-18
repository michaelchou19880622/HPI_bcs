package com.hpicorp.bcs.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageTemplate;

@Repository
public interface MessageTemplateRepository extends PagingAndSortingRepository<MessageTemplate, Long> {

	@Query(value = "select M from MessageTemplate M order by M.id desc ")
	public List<MessageTemplate> getMessageTeamplateByType();
	
	@Query(value = "select M from MessageTemplate M where M.type = 'buttons' or M.type = 'confirm' order by M.id desc ")
	public Page<MessageTemplate> getMessageTeamplateByType(Pageable pageable);
	
}
