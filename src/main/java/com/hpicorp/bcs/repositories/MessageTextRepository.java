package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageText;

@Repository
public interface MessageTextRepository extends JpaRepository<MessageText, Long> {

	public boolean existsById(Long messageId);


}
