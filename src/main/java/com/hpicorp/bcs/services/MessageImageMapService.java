package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.MessageImageMap;
import com.hpicorp.core.entities.MessageImageMapAction;
import com.hpicorp.core.repository.MessageImageMapRepository;
import com.hpicorp.core.repository.MessageImagemapActionRepository;

@Service
public class MessageImageMapService {

	@Autowired
	private MessageImageMapRepository messageImageMapRepository;
	
	@Autowired
	private MessageImagemapActionRepository messageImagemapActionRepository;

	public Page<MessageImageMap> getAllMessageImageMap(Pageable pageable) {
		return messageImageMapRepository.findAll(pageable);
	}

	public List<MessageImageMap> getAllMessageImageMapByType(String type) {
		return messageImageMapRepository.getMessageImageMapByType(type);
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
	
	public void removeActions(List<MessageImageMapAction> originalActionList) {
		this.messageImagemapActionRepository.deleteInBatch(originalActionList);
		this.messageImagemapActionRepository.flush();
	}
	
	public boolean checkImageMap(MessageImageMap messageImageMap) {
		if(messageImageMap.getType() == null || messageImageMap.getType().isEmpty() ||
				messageImageMap.getBaseUrl() == null || messageImageMap.getBaseUrl().isEmpty() ||
				!messageImageMap.getBaseUrl().toLowerCase().startsWith("https") ||
				messageImageMap.getAltText() == null || messageImageMap.getAltText().isEmpty() ||
				messageImageMap.getBaseSizeWidth() == null || messageImageMap.getBaseSizeWidth() != 1040 ||
				messageImageMap.getBaseSizeHeight() == null || messageImageMap.getBaseSizeHeight() == 0 ||
				messageImageMap.getMessageImageMapActionList() == null ||
				messageImageMap.getMessageImageMapActionList().isEmpty() ||
				messageImageMap.getMessageImageMapActionList().size() > 50
				) {
			return false;
		}
		
		for (MessageImageMapAction messageImageMapAction : messageImageMap.getMessageImageMapActionList()) {
			if(messageImageMapAction.getType() == null || messageImageMapAction.getType().isEmpty() ||
					messageImageMapAction.getAreaX() == null || messageImageMapAction.getAreaX() < 0 ||
					messageImageMapAction.getAreaY() == null || messageImageMapAction.getAreaY() < 0 ||
					messageImageMapAction.getAreaHeight() == null || messageImageMapAction.getAreaHeight() < 0 ||
					messageImageMapAction.getAreaWidth() == null || messageImageMapAction.getAreaWidth() < 0) {
				return false;
			}
			if(messageImageMapAction.getType().equals("uri")) {
				if(messageImageMapAction.getLinkUri() == null || messageImageMapAction.getLinkUri().isEmpty()) {
					return false;
				}
				if(!messageImageMapAction.getLinkUri().startsWith("http") &&
						!messageImageMapAction.getLinkUri().startsWith("https") &&
						!messageImageMapAction.getLinkUri().startsWith("line") &&
						!messageImageMapAction.getLinkUri().startsWith("tel") ) {
					return false;
				}
			}
			if(messageImageMapAction.getType().equals("message") && 
					(messageImageMapAction.getText() == null || messageImageMapAction.getText().isEmpty())) {
				return false;
			}
		}
		
		return true;
	}

}
