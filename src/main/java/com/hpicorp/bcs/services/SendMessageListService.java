package com.hpicorp.bcs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.SendMessageList;
import com.hpicorp.core.repository.SendMessageListRepository;

@Service
public class SendMessageListService {

	@Autowired
	private SendMessageListRepository sendMessageListRepository;

	public List<SendMessageList> getSendMessageListBySendID(Long id) {
		return sendMessageListRepository.getSendMessageListBySendID(id);
	}

	public void deleteById(Long id) {
		sendMessageListRepository.deleteById(id);
	}

	public void insert(SendMessageList sendMessageList) {
		sendMessageListRepository.save(sendMessageList);
	}
}
