package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.SendMessage;

@Repository
public interface SendMessageRepository extends PagingAndSortingRepository<SendMessage, Long> {

	@Query(value = "select * from send_message where mode in :mode ", nativeQuery = true)
	public Page<SendMessage> getSendMessageByMode(Pageable pageable, @Param("mode") List<String> mode);

	@Query(value = "select * from send_message where status = :status ", nativeQuery = true)
	public Page<SendMessage> getSendMessageByStatus(Pageable pageable, @Param("status") int status);

}
