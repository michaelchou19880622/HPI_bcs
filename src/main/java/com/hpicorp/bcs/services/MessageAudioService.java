package com.hpicorp.bcs.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hpicorp.bcs.entities.MessageAudio;
import com.hpicorp.bcs.repositories.MessageAudioRepository;

@Service
public class MessageAudioService {

	@Autowired
	private MessageAudioRepository messageAudioRepository ;
	
	public Optional<MessageAudio> findById(long id) {
		return messageAudioRepository.findById(id);
	}
	
	public void insert(MessageAudio messageAudio) {
		messageAudioRepository.save(messageAudio);
	}
	
}
