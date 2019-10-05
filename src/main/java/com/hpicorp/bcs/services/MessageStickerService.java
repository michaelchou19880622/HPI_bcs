package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageSticker;
import com.hpicorp.bcs.repositories.MessageStickerRepository;

@Service
public class MessageStickerService {

	@Autowired
	private MessageStickerRepository messageStickerRepository;

	public List<MessageSticker> getmessageSticker() {
		return messageStickerRepository.findAll();
	}

	public Optional<MessageSticker> findById(long id) {
		return messageStickerRepository.findById(id);
	}

	public void insert(MessageSticker messageSticker) {
		messageStickerRepository.save(messageSticker);
	}
}
