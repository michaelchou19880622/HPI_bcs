package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.MessageCarouselTemplate;
import com.hpicorp.bcs.repositories.MessageCarouselActionRepository;
import com.hpicorp.bcs.repositories.MessageCarouselColumnRepository;
import com.hpicorp.bcs.repositories.MessageCarouselTemplateRepository;

@Service
public class MessageCarouselTemplateService {

	@Autowired
	private MessageCarouselTemplateRepository messageCarouselTemplateRepository;

	@Autowired
	private MessageCarouselColumnRepository messageCarouselColumnRepository;

	@Autowired
	private MessageCarouselActionRepository messageCarouselActionRepository;

	public List<MessageCarouselTemplate> getAllMessageCarouselTemplate() {
		return messageCarouselTemplateRepository.findAll();
	}
	
	public Page<MessageCarouselTemplate> getAllMessageCarouselTemplate(Pageable pageable) {
		return messageCarouselTemplateRepository.findAll(pageable);
	}

	public void insert(MessageCarouselTemplate messageCarouselTemplate) {
		messageCarouselTemplateRepository.save(messageCarouselTemplate);
	}

	public void save(MessageCarouselTemplate messageTemplate) {
		messageCarouselTemplateRepository.save(messageTemplate);
	}

	public Optional<MessageCarouselTemplate> findById(long id) {
		return messageCarouselTemplateRepository.findById(id);
	}

	public void deleteById(long id) {
		messageCarouselTemplateRepository.deleteById(id);
	}

	public void deleteAction(long id) {
		messageCarouselActionRepository.deleteById(id);
	}

	public void deleteColumn(long id) {
		messageCarouselColumnRepository.deleteById(id);
	}
}
