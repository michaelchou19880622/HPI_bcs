package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageVideo;

@Repository
public interface MessageVideoRepository extends JpaRepository<MessageVideo, Long> {

	public boolean existsById(Long messageId);

}
