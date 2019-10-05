package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageText;
import com.hpicorp.bcs.repositories.MessageTextRepository;

@Service
public class MessageTextService {

	@Autowired
	private MessageTextRepository messageTextRepository;

	public List<MessageText> getAllMessageText() {
		return messageTextRepository.findAll();
	}

	public Optional<MessageText> findById(long id) {
		return messageTextRepository.findById(id);
	}

	public void insert(MessageText messageText) {
		messageTextRepository.save(messageText);
	}
}
