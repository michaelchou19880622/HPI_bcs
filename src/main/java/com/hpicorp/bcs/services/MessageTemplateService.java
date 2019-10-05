package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageTemplate;
import com.hpicorp.bcs.repositories.MessageTemplateRepository;

@Service
public class MessageTemplateService {

	@Autowired
	private MessageTemplateRepository messageTemplateRepository;

	public Page<MessageTemplate> getAllMessageTemplate(Pageable pageable) {
		return messageTemplateRepository.findAll(pageable);
	}

	public List<MessageTemplate> getAllMessageTemplate() {
		return messageTemplateRepository.getMessageTeamplateByType("LINK");
	}

	public MessageTemplate insert(MessageTemplate messageTemplate) {
		return messageTemplateRepository.save(messageTemplate);
	}

	public void save(MessageTemplate messageTemplate) {
		messageTemplateRepository.save(messageTemplate);
	}

	public Optional<MessageTemplate> findById(Integer id) {
		return messageTemplateRepository.findById(id);
	}

	public void deleteById(Long id) {
		messageTemplateRepository.deleteById(id.intValue());
	}

	public void deleteByTemplateID(long id) {
		messageTemplateRepository.deleteByTemplateID(id);
	}
}
