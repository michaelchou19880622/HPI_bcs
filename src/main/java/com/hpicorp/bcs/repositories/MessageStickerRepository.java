package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageSticker;

@Repository
public interface MessageStickerRepository extends JpaRepository<MessageSticker, Long> {

	public boolean existsById(Long messageId);

}
