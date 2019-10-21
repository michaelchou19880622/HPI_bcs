package com.hpicorp.bcs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.MessageVideo;
import com.hpicorp.core.repository.MessageVideoRepository;

@Service
public class MessageVideoService {

	@Autowired
	private MessageVideoRepository messageVideoRepository;

	public Optional<MessageVideo> findById(long id) {
		return messageVideoRepository.findById(id);
	}

	public void insert(MessageVideo messageVideo) {
		messageVideoRepository.save(messageVideo);
	}
}
