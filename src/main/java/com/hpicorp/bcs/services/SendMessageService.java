package com.hpicorp.bcs.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.MessageAudio;
import com.hpicorp.bcs.entities.MessageImage;
import com.hpicorp.bcs.entities.MessageSticker;
import com.hpicorp.bcs.entities.MessageTemplate;
import com.hpicorp.bcs.entities.MessageTemplateAction;
import com.hpicorp.bcs.entities.MessageText;
import com.hpicorp.bcs.entities.MessageVideo;
import com.hpicorp.bcs.entities.SendMessage;
import com.hpicorp.bcs.entities.SendMessageList;
import com.hpicorp.bcs.enums.MessageType;
import com.hpicorp.bcs.repositories.LineUserGroupRepository;
import com.hpicorp.bcs.repositories.SendMessageListRepository;
import com.hpicorp.bcs.repositories.SendMessageRepository;
import com.hpicorp.bcs.repositories.SendMessageUsersRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendMessageService {

	@Autowired
	private SendMessageRepository sendMessageRepository;

	@Autowired
	private SendMessageListRepository sendMessageListRepository;

	@Autowired
	private SendMessageUsersRepository sendMessageUsersRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	LineUserGroupRepository lineUserGroupRepository;

	@Autowired
	private MessageTextService messageTextService;

	@Autowired
	private MessageStickerService messageStickerService;

	@Autowired
	private MessageVideoService messageVideoService;

	@Autowired
	private MessageAudioService messageAudioService;

	@Autowired
	private MessageImageService messageImageService;

	@Autowired
	private MessageTemplateService messageTemplateService;
	
	public Page<SendMessage> getAllSendMessage(Pageable pageable) {
		return sendMessageRepository.findAll(pageable);
	}

	public Page<SendMessage> getAllSendMessage(Pageable pageable, String type) {
		return sendMessageRepository.findByType(pageable, type);
	}

	public Page<SendMessage> getAllSendMessageByMode(Pageable pageable, List<String> keys) {
		return sendMessageRepository.getSendMessageByMode(pageable, keys);
	}

	public Page<SendMessage> getAllSendMessageByStatus(Pageable pageable, int status) {
		return sendMessageRepository.getSendMessageByStatus(pageable, status);
	}

	public List<SendMessage> getAllSendMessage() {
		return (List<SendMessage>) sendMessageRepository.findAll();
	}

	public void insert(SendMessage sendMessage) {
		sendMessageRepository.save(sendMessage);
	}

	public void save(SendMessage sendMessage) {
		sendMessageRepository.save(sendMessage);
	}

	public Optional<SendMessage> findById(long id) {
		return sendMessageRepository.findById(id);
	}

	public void deleteById(long id) {
		sendMessageUsersRepository.deleteBySendID(id);
		sendMessageRepository.deleteById(id);
	}

	public void deleteListBySendyId(long id) {
		sendMessageListRepository.deleteBySendID(id);
	}

	@SuppressWarnings("unchecked")
	public List<BigInteger> getLineUserGroupListById(long lineUserGroupId) {
		List<BigInteger> result = new ArrayList<>();

		String getUserSql = lineUserGroupRepository.findgetUsersById(lineUserGroupId);
		getUserSql = getUserSql.replaceAll("select line_uid from lineuser", "select id from lineuser");
		try {
			Query query = em.createNativeQuery(getUserSql);
			result = query.getResultList();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return result;
	}

	@Transactional
	public int insSendMessageUser(String sqlString) {
		int result = -1;
		try {
			Query query = em.createNativeQuery(sqlString);
			result = query.executeUpdate();
		} catch (Exception e) {
			log.error("Exception : ", e);
		}
		return result;
	}

	public SendMessage saveSendMessage(SendMessage sendMessage) {
		for (SendMessageList sendMessageList : sendMessage.getSendMessageList()) {
			String[] typelist = sendMessageList.getMessageType().split(";");
			if (typelist[0].trim().equals(MessageType.TEXT.getValue())) {
				MessageText txt = sendMessageList.getMessageTextList().get(0);
				messageTextService.insert(txt);
				sendMessageList.setMessageId(Integer.parseInt(txt.getId().toString()));
			} else if (typelist[0].trim().equals(MessageType.IMAGE.getValue())) {
				MessageImage img = sendMessageList.getMessageImageList().get(0);
				messageImageService.insert(img);
				sendMessageList.setMessageId(Integer.parseInt(img.getId().toString()));
			} else if (typelist[0].trim().equals(MessageType.VIDEO.getValue())) {
				MessageVideo video = sendMessageList.getMessageVideoList().get(0);
				messageVideoService.insert(video);
				sendMessageList.setMessageId(Integer.parseInt(video.getId().toString()));
			} else if (typelist[0].trim().equals(MessageType.AUDIO.getValue())) {
				MessageAudio audio = sendMessageList.getMessageAudioList().get(0);
				messageAudioService.insert(audio);
				sendMessageList.setMessageId(Integer.parseInt(audio.getId().toString()));
			} else if (typelist[0].trim().equals(MessageType.STICKER.getValue())) {
				MessageSticker sticker = sendMessageList.getMessageStickerList().get(0);
				messageStickerService.insert(sticker);
				sendMessageList.setMessageId(Integer.parseInt(sticker.getId().toString()));
			} else if (typelist[0].trim().equals(MessageType.LINK.getValue())) {
				MessageTemplate messageTemplate = sendMessageList.getMessageTemplateList().get(0);
				for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
					d.setMessageTemplate(messageTemplate);
				}
				messageTemplateService.insert(messageTemplate);
				sendMessageList.setMessageId(Integer.parseInt(messageTemplate.getId().toString()));
				typelist[0] = MessageType.TEMPLATE.getValue();
			}
			sendMessageList.setOrderIndex(Integer.parseInt(typelist[1], 10));
			sendMessageList.setMessageType(typelist[0].trim());
			sendMessageList.setSendMessage(sendMessage);
		}
		sendMessage.setCreationTime(new Date());
		sendMessage.setModifyTime(new Date());
		SendMessage createdSendMessage = sendMessageRepository.save(sendMessage);
		return createdSendMessage;
	}

}
