package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.SendMessageUsers;

@Repository
public interface SendMessageUsersRepository extends JpaRepository<SendMessageUsers, Long> {

	@Modifying
	@Transactional
	@Query(value = "delete from send_message_users where send_id = :sendId ", nativeQuery = true)
	public void deleteBySendID(@Param("sendId") long sendId);

	@Query(value = " :sqlcmd ", nativeQuery = true)
	public void insertSendMessageUsers(@Param("sqlcmd") String sqlcmd);

}
