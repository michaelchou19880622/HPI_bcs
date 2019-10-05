package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageVideo;
import com.hpicorp.bcs.repositories.MessageVideoRepository;

@Service
public class MessageVideoService {

	@Autowired
	private MessageVideoRepository messageVideoRepository;

	public List<MessageVideo> getAllMessageVideo() {
		return messageVideoRepository.findAll();
	}

	public Optional<MessageVideo> findById(long id) {
		return messageVideoRepository.findById(id);
	}

	public void insert(MessageVideo messageVideo) {
		messageVideoRepository.save(messageVideo);
	}
}
