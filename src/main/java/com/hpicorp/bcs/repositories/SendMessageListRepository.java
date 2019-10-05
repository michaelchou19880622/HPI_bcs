package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.SendMessageList;

@Repository
public interface SendMessageListRepository extends JpaRepository<SendMessageList, Long> {

	@Modifying
	@Transactional
	@Query(value = "delete from send_message_list where send_id = :sendId ", nativeQuery = true)
	public void deleteBySendID(@Param("sendId") long sendId);

	@Modifying
	@Transactional
	@Query(value = "select S from SendMessageList S where S.sendMessage.id = :sendId order by S.orderIndex ")
	public List<SendMessageList> getSendMessageListBySendID(@Param("sendId") long sendId);
	
}
