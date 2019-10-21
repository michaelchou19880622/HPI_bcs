package com.hpicorp.bcs.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hpicorp.bcs.services.MessageAudioService;
import com.hpicorp.bcs.services.MessageCarouselTemplateService;
import com.hpicorp.bcs.services.MessageImageMapService;
import com.hpicorp.bcs.services.MessageImageService;
import com.hpicorp.bcs.services.MessageStickerService;
import com.hpicorp.bcs.services.MessageTemplateService;
import com.hpicorp.bcs.services.MessageTextService;
import com.hpicorp.bcs.services.MessageVideoService;
import com.hpicorp.bcs.services.SendMessageListService;
import com.hpicorp.bcs.services.SendMessageService;
import com.hpicorp.core.entities.MessageAudio;
import com.hpicorp.core.entities.MessageCarouselAction;
import com.hpicorp.core.entities.MessageCarouselColumn;
import com.hpicorp.core.entities.MessageCarouselTemplate;
import com.hpicorp.core.entities.MessageImage;
import com.hpicorp.core.entities.MessageImageMap;
import com.hpicorp.core.entities.MessageSticker;
import com.hpicorp.core.entities.MessageTemplate;
import com.hpicorp.core.entities.MessageTemplateAction;
import com.hpicorp.core.entities.MessageText;
import com.hpicorp.core.entities.MessageVideo;
import com.hpicorp.core.entities.SendMessage;
import com.hpicorp.core.entities.SendMessageList;
import com.hpicorp.core.entities.SystemUser;
import com.hpicorp.core.enums.MessageTypes;
import com.hpicorp.core.repository.LineUserRepository;
import com.hpicorp.core.repository.SystemUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class SendMessageController {

	private static final String STATUS = "status";

	@Autowired
	private SendMessageService sendMessageService;
	
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
	private MessageImageMapService messageImageMapService;
	
	@Autowired
	private MessageTemplateService messageTemplateService;
	
	@Autowired
	private MessageCarouselTemplateService messageCarouselTemplateService;

	@Autowired
	private SendMessageListService sendMessageListService;

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Autowired
	private LineUserRepository lineUserRepository;

	@PersistenceContext
	private EntityManager em;

	/**
	 * [Read List]訊息列表
	 * @param pageable
	 * @return
	 */
	@GetMapping(path = "/sendMessage/all")
	public Page<SendMessage> getAll(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
		return sendMessageService.getAllSendMessage(pageable);
	}
	
	/**
	 * [Read List]訊息列表(分發送時間狀態)
	 * @param pageable
	 * @param key
	 * @return
	 */
	@GetMapping(path = "/sendMessage/mode/{key}")
	public Page<SendMessage> getAllSendMessageByMode(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("key") String key) {
		List<String> selectedValues = new ArrayList<>();
		if (key.equals("reserve")) {
			selectedValues.add("ONCE");
		} else if (key.equals("schedule")) {
			selectedValues.add("EVERYDAY");
			selectedValues.add("WEEKLY");
			selectedValues.add("MONTHLY");
		}
		return sendMessageService.getAllSendMessageByMode(pageable, selectedValues);
	}

	/**
	 * [Read List]訊息列表(分發送狀態)
	 * @param pageable
	 * @param key
	 * @return
	 */
	@GetMapping(path = "/sendMessage/status/{key}")
	public Page<SendMessage> getAllSendMessageByStatus(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable("key") String key) {
		int istatus = 0;
		if (key.equals("draft")) {
			istatus = -1;
		} else if (key.equals("sent")) {
			istatus = 8;
		}
		return sendMessageService.getAllSendMessageByStatus(pageable, istatus);
	}

	/**
	 * [Create]新增發送訊息
	 * @param sendMessage
	 * @return
	 */
	@PostMapping(path = "/sendMessage/new")
	public Map<String, String> createSendMessage(@RequestBody SendMessage sendMessage) {
		log.info(sendMessage.getSendMessageList().toString());

		for (SendMessageList am : sendMessage.getSendMessageList()) {
			log.info("getMessageVideoList:" + am.getMessageType());
			String[] typelist = am.getMessageType().split(";");
			if (typelist[0].trim().equals(MessageTypes.TEXT.getValue())) {
				MessageText txt = am.getMessageTextList().get(0);
				messageTextService.insert(txt);
				am.setMessageId(txt.getId());
			} else if (typelist[0].trim().equals(MessageTypes.IMAGE.getValue())) {
				MessageImage img = am.getMessageImageList().get(0);
				messageImageService.insert(img);
				am.setMessageId(img.getId());
			} else if (typelist[0].trim().equals(MessageTypes.VIDEO.getValue())) {
				MessageVideo video = am.getMessageVideoList().get(0);
				messageVideoService.insert(video);
				am.setMessageId(video.getId());
			} else if (typelist[0].trim().equals(MessageTypes.AUDIO.getValue())) {
				MessageAudio audio = am.getMessageAudioList().get(0);
				messageAudioService.insert(audio);
				am.setMessageId(audio.getId());
			} else if (typelist[0].trim().equals(MessageTypes.STICKER.getValue())) {
				MessageSticker sticker = am.getMessageStickerList().get(0);
				messageStickerService.insert(sticker);
				am.setMessageId(sticker.getId());
			} else {
			}
			am.setOrderIndex(Integer.parseInt(typelist[1], 10));
			am.setMessageType(typelist[0].trim());
			am.setSendMessage(sendMessage);
		}
		sendMessage.setCreationTime(new Date());
		sendMessage.setModifyTime(new Date());
		sendMessageService.insert(sendMessage);
		long newId = sendMessage.getId();
		Long groupID = sendMessage.getGroupId();
		StringBuilder sb = new StringBuilder();
		if (groupID.compareTo(-101L) == 0) { // Send to me
			String account = sendMessage.getModifyAccount();
			Optional<SystemUser> sUser = systemUserRepository.findByAccount(account);
			if (sUser.isPresent()) {
				Long uid = lineUserRepository.getIdByUid(sUser.get().getLineuserUid());
				if (uid != null) {
					sb.append("insert into send_message_users (send_id, line_user_id) values ");
					sb.append(String.format("(%s,%s)", newId, uid));
					log.info("SQL => " + sb.toString());
					sendMessageService.insSendMessageUser(sb.toString());
				}
			}
		} else if (groupID.compareTo(-9L) == 0) { // All
			sb.append("insert into send_message_users (send_id, line_user_id) ");
			sb.append("select " + newId + ", id from lineuser where length(line_uid) = 33 ;");
			log.info("SQL => " + sb.toString());
			sendMessageService.insSendMessageUser(sb.toString());
		} else if (groupID.compareTo(-99L) == 0) { // Send to Test Group
			sb.append("select id from lineuser where line_uid in (select lineuser_uid from systemuser where length(lineuser_uid) = 33)");
			log.info("SQL:=> " + sb.toString());
		} else if (groupID.compareTo(0L) > 0) {
			List<BigInteger> lineUserList = sendMessageService.getLineUserGroupListById(groupID);
			sb.append("insert into send_message_users (send_id, line_user_id) values ");
			int idx = 0;
			for (BigInteger lid : lineUserList) {
				if (idx > 0)
					sb.append(",");
				sb.append(String.format("(%s,%s)", String.valueOf(newId), String.valueOf(lid)));
				idx++;
			}
			log.info("SQL:" + sb.toString());
			sendMessageService.insSendMessageUser(sb.toString());
		}

		Map<String, String> mapped = new HashMap<>();
		mapped.put(STATUS, "Success");
		mapped.put("id", String.valueOf(sendMessage.getId()));
		return mapped;
	}

	/**
	 * [Delete]刪除訊息
	 * @param id
	 */
	@DeleteMapping("/sendMessage/{id}")
	public void deleteSendMessage(@PathVariable long id) {
		log.info("deleteMessageImageMap:" + id);
		sendMessageService.deleteById(id);
	}

	/**
	 * [Update]更新訊息內容
	 * @param sendMessage
	 * @param id
	 * @return
	 */
	@PutMapping(path = "/sendMessage/{id}")
	public @ResponseBody Map<String, String> updateMessage(@RequestBody SendMessage sendMessage,
			@PathVariable long id) {

		Optional<SendMessage> sendMessageOptional = sendMessageService.findById(id);

		Map<String, String> mapped = new HashMap<>();

		if (!sendMessageOptional.isPresent()) {
			mapped.put(STATUS, "Failure");
			return mapped;
		}
		List<SendMessageList> list = sendMessageListService.getSendMessageListBySendID(id);
		// deleteUnused(sendMessage, list);

		sendMessage.setId(id);
		for (SendMessageList am : sendMessage.getSendMessageList()) {
			am.setSendMessage(sendMessage);
			String[] typelist = am.getMessageType().split(";");
			boolean runUpdate = true;

			if (typelist[0].trim().equals(MessageTypes.TEXT.getValue())) {
				MessageText txt = am.getMessageTextList().get(0);
				messageTextService.insert(txt);
				am.setMessageId(txt.getId());
			} else if (typelist[0].trim().equals(MessageTypes.IMAGE.getValue())) {
				MessageImage img = am.getMessageImageList().get(0);
				messageImageService.insert(img);
				am.setMessageId(img.getId());
			} else if (typelist[0].trim().equals(MessageTypes.VIDEO.getValue())) {
				MessageVideo video = am.getMessageVideoList().get(0);
				messageVideoService.insert(video);
				am.setMessageId(video.getId());
			} else if (typelist[0].trim().equals(MessageTypes.AUDIO.getValue())) {
				MessageAudio audio = am.getMessageAudioList().get(0);
				messageAudioService.insert(audio);
				am.setMessageId(audio.getId());
			} else if (typelist[0].trim().equals(MessageTypes.STICKER.getValue())) {
				MessageSticker sticker = am.getMessageStickerList().get(0);
				messageStickerService.insert(sticker);
				am.setMessageId(sticker.getId());
			} else if (typelist[0].trim().equals(MessageTypes.TEMPLATE.getValue())) {
				for (SendMessageList am1 : list) {
					if (am1.getId() == am.getId() && am1.getMessageId() == am.getMessageId())
						runUpdate = false;
				}
			}
			am.setOrderIndex(Integer.parseInt(typelist[1], 10));
			am.setMessageType(typelist[0].trim());
			if (runUpdate) {
				sendMessageListService.insert(am);
			}
		}
		System.out.println("sendMessage.MessageList:" + sendMessage.getSendMessageList().size());
		System.out.println("sendMessageService save");
		sendMessage.setModifyTime(new Date());
		sendMessageService.save(sendMessage);

		mapped.put("status", "Success");
		mapped.put("id", String.valueOf(sendMessage.getId()));
		return mapped;
	}

	/**
	 * [Read]取得該發送訊息的訊息清單
	 * @param sendMessageID
	 * @return
	 */
	@GetMapping(path = "/sendMessage/getList/{sendMessageID}")
	public Map<String, Map<String, String>> getAllAutoreplyMessageListByID(
			@PathVariable("sendMessageID") String sendMessageID) {
		Optional<SendMessage> SendMessageOptional = sendMessageService.findById(Long.parseLong(sendMessageID));

		List<SendMessageList> list = SendMessageOptional.get().getSendMessageList();

		String txtmsg = "";
		String imageurl = "";
		String previewurl = "";
		String stickerurl = "";
		String packageid = "";
		String stickerid = "";
		String videourl = "";
		String audiourl = "";
		String audioDuration = "";
		String imagemapid = "";
		String templateid = "";
		String titleText = "";
		String templatetype = "";
		String msgType = "";
		String linkurl = "";
		String memo = "";
		String id = "";
		Map<String, Map<String, String>> mappedList = new HashMap<>();
		if (list.size() > 0) {
			for (SendMessageList am : list) {
				id = String.valueOf(am.getId());
				msgType = am.getMessageType();
				if (am.getMessageType().equals(MessageTypes.TEXT.getValue())) {
					Optional<MessageText> obj = messageTextService.findById(am.getMessageId().longValue());
					txtmsg = obj.get().getText();
				}
				if (am.getMessageType().equals(MessageTypes.IMAGE.getValue())) {
					Optional<MessageImage> obj = messageImageService.findById(am.getMessageId().longValue());
					imageurl = obj.get().getOriginalContentUrl();
					previewurl = obj.get().getPreviewImageUrl();
				}
				if (am.getMessageType().equals(MessageTypes.VIDEO.getValue())) {
					Optional<MessageVideo> obj = messageVideoService.findById(am.getMessageId().longValue());
					videourl = obj.get().getOriginalContentUrl();
					previewurl = obj.get().getPreviewImageUrl();
				}
				if (am.getMessageType().equals(MessageTypes.AUDIO.getValue())) {
					Optional<MessageAudio> obj = messageAudioService.findById(am.getMessageId().longValue());
					audiourl = obj.get().getOriginalContentUrl();
					audioDuration = String.valueOf(obj.get().getDuration());
				}
				if (am.getMessageType().equals(MessageTypes.STICKER.getValue())) {
					Optional<MessageSticker> obj = messageStickerService.findById(am.getMessageId().longValue());
					packageid = String.valueOf(obj.get().getPackageId());
					stickerid = obj.get().getStickerId();
				}
				if (am.getMessageType().equals(MessageTypes.IMAGEMAP.getValue())) {
					Optional<MessageImageMap> obj = messageImageMapService.findById(am.getMessageId());
					imagemapid = String.valueOf(am.getMessageId());
					titleText = obj.get().getAltText();
					imageurl = obj.get().getBaseUrl();
				}
				if (am.getMessageType().equals(MessageTypes.TEMPLATE.getValue())) {
					templateid = String.valueOf(am.getMessageId());
					Optional<MessageTemplate> obj = messageTemplateService.findById(am.getMessageId());
					titleText = obj.get().getAltText();
					imageurl = obj.get().getThumbnailImageUrl();
					templatetype = obj.get().getType();
					for (MessageTemplateAction d : obj.get().getMessageTemplateActionList()) {
						memo += d.getLabel() + ";";
					}
					stickerurl = obj.get().getText();
				}
				JsonArray colobj = new JsonArray();
				List<MessageCarouselColumn> columnlist = new ArrayList<MessageCarouselColumn>();
				if (am.getMessageType().equals(MessageTypes.CAROUSEL.getValue())) {
					templateid = String.valueOf(am.getMessageId());
					Optional<MessageCarouselTemplate> obj = messageCarouselTemplateService.findById(am.getMessageId());
					titleText = obj.get().getAltText();
					imageurl = obj.get().getMessageCarouseColumnList().get(0).getThumbnailImageUrl();
					templatetype = obj.get().getType();
					for (MessageCarouselColumn col : obj.get().getMessageCarouseColumnList()) {
						columnlist.add(col);
						JsonObject c = new JsonObject();
						c.addProperty("text", col.getText());
						c.addProperty("imagurl", col.getThumbnailImageUrl());
						c.addProperty("title", col.getTitle());
						String actString = "";
						for (MessageCarouselAction a : col.getMessageCarouselActionList())
							actString += a.getLabel() + "|";
						c.addProperty("memo", actString);
						colobj.add(c);
					}
				}
				Map<String, String> mapped = new HashMap<>();
				mapped.put("id", id);
				mapped.put("type", msgType);
				mapped.put("txtmsg", txtmsg);
				mapped.put("imageurl", imageurl);
				mapped.put("previewur", previewurl);
				mapped.put("videourl", videourl);
				mapped.put("titleText", titleText);
				mapped.put("linkurl", linkurl);
				mapped.put("memo", memo);
				mapped.put("audiourl", audiourl);
				mapped.put("audioDuration", audioDuration);
				mapped.put("imagemapid", imagemapid);
				mapped.put("templateid", templateid);
				mapped.put("templatetype", templatetype);
				mapped.put("stickerurl", stickerurl);
				mapped.put("packageid", packageid);
				mapped.put("stickerid", stickerid);
				mapped.put("visibility", "true");
				mappedList.put("msg" + am.getOrderIndex(), mapped);
			}
		}
		return mappedList;
	}

}
