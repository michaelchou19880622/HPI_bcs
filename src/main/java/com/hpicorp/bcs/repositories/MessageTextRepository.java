package com.hpicorp.bcs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageText;

@Repository
public interface MessageTextRepository extends JpaRepository<MessageText, Long> {

	public boolean existsById(Long messageId);

	@Query(value = "select mt from MessageText mt " 
				 + "join AutoreplyMessageList aml on mt.id = aml.messageId "
				 + "join Autoreply a on aml.autoreplyId = a.id " 
				 + "where a.keyword = :keyword")
	public Optional<MessageText> findByKeyword(@Param("keyword") String keyword);

}
