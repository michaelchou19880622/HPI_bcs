package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageImageMap;
import com.hpicorp.bcs.repositories.MessageImageMapRepository;

@Service
public class MessageImageMapService {

	@Autowired
	private MessageImageMapRepository messageImageMapRepository;

	public Page<MessageImageMap> getAllMessageImageMap(Pageable pageable) {
		return messageImageMapRepository.findAll(pageable);
	}

	public List<MessageImageMap> getAllMessageImageMap() {
		Sort sortSpec = orderBy();
		return (List<MessageImageMap>) messageImageMapRepository.findAll(sortSpec);
	}

	public List<MessageImageMap> getAllMessageImageMapByType(String type) {
		return messageImageMapRepository.getMessageImageMapByType(type);
	}

	private Sort orderBy() {
		return new Sort(Sort.Direction.DESC, "id");
	}

	public void insert(MessageImageMap messageImageMap) {
		messageImageMapRepository.save(messageImageMap);
	}

	public void save(MessageImageMap messageImageMap) {
		messageImageMapRepository.save(messageImageMap);
	}

	public Optional<MessageImageMap> findById(Long id) {
		return messageImageMapRepository.findById(id);
	}

	public void deleteById(Long id) {
		messageImageMapRepository.deleteById(id);
	}

}
