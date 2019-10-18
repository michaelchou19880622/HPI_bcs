package com.hpicorp.bcs.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hpicorp.bcs.entities.MessageImage;
import com.hpicorp.bcs.repositories.MessageImageRepository;

@Service
public class MessageImageService {

	@Autowired
	private MessageImageRepository messageImageRepository;

	public Optional<MessageImage> findById(long id) {
		return messageImageRepository.findById(id);
	}

	public void insert(MessageImage messageImage) {
		messageImageRepository.save(messageImage);
	}
}
