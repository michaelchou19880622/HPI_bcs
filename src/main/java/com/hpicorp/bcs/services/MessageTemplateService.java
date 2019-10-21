package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.MessageTemplate;
import com.hpicorp.core.entities.MessageTemplateAction;
import com.hpicorp.core.enums.MessageTemplateTypes;
import com.hpicorp.core.repository.MessageTemplateActionRepository;
import com.hpicorp.core.repository.MessageTemplateRepository;

@Service
public class MessageTemplateService {

	@Autowired
	private MessageTemplateRepository messageTemplateRepository;
	
	@Autowired
	private MessageTemplateActionRepository messageTemplateActionRepository;

	public List<MessageTemplate> getAllMessageTemplateByType() {
		return messageTemplateRepository.getMessageTeamplateByType();
	}
	
	public Page<MessageTemplate> getAllMessageTemplateByType(Pageable pageable) {
		return messageTemplateRepository.getMessageTeamplateByType(pageable);
	}

	public MessageTemplate insert(MessageTemplate messageTemplate) {
		return messageTemplateRepository.save(messageTemplate);
	}

	public void save(MessageTemplate messageTemplate) {
		messageTemplateRepository.save(messageTemplate);
	}

	public Optional<MessageTemplate> findById(Long id) {
		return messageTemplateRepository.findById(id);
	}

	public void deleteById(Long id) {
		messageTemplateRepository.deleteById(id);
	}
	
	public boolean checkTemplate(MessageTemplate messageTemplate) {
		if(messageTemplate.getMessageTemplateActionList() == null ||
				messageTemplate.getMessageTemplateActionList().isEmpty())
			return false;
		if(messageTemplate.getType().equals(MessageTemplateTypes.BUTTONS.toString()) &&
				messageTemplate.getMessageTemplateActionList().size() > 4) {
			return false;
		}
		if(messageTemplate.getType().equals(MessageTemplateTypes.CONFIRM.toString()) &&
				messageTemplate.getMessageTemplateActionList().size() != 2) {
			return false;
		}
		return true;
	}

	public void removeActions(List<MessageTemplateAction> messageTemplateActionList) {
		this.messageTemplateActionRepository.deleteInBatch(messageTemplateActionList);
		this.messageTemplateActionRepository.flush();
		
	}	
}
