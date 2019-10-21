package com.hpicorp.bcs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.MessageSticker;
import com.hpicorp.core.repository.MessageStickerRepository;

@Service
public class MessageStickerService {

	@Autowired
	private MessageStickerRepository messageStickerRepository;

	public Optional<MessageSticker> findById(long id) {
		return messageStickerRepository.findById(id);
	}

	public void insert(MessageSticker messageSticker) {
		messageStickerRepository.save(messageSticker);
	}
}
