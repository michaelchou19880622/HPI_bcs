package com.hpicorp.bcs.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hpicorp.bcs.entities.AutoreplyMessageList;
import com.hpicorp.bcs.entities.MessageAudio;
import com.hpicorp.bcs.entities.MessageCarouselAction;
import com.hpicorp.bcs.entities.MessageCarouselColumn;
import com.hpicorp.bcs.entities.MessageCarouselTemplate;
import com.hpicorp.bcs.entities.MessageImage;
import com.hpicorp.bcs.entities.MessageImageMap;
import com.hpicorp.bcs.entities.MessageSticker;
import com.hpicorp.bcs.entities.MessageTemplate;
import com.hpicorp.bcs.entities.MessageTemplateAction;
import com.hpicorp.bcs.entities.MessageText;
import com.hpicorp.bcs.entities.MessageVideo;
import com.hpicorp.bcs.enums.MessageType;
import com.hpicorp.bcs.services.AutoreplyMessageListService;
import com.hpicorp.bcs.services.MessageAudioService;
import com.hpicorp.bcs.services.MessageCarouselTemplateService;
import com.hpicorp.bcs.services.MessageImageMapService;
import com.hpicorp.bcs.services.MessageImageService;
import com.hpicorp.bcs.services.MessageStickerService;
import com.hpicorp.bcs.services.MessageTemplateService;
import com.hpicorp.bcs.services.MessageTextService;
import com.hpicorp.bcs.services.MessageVideoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageAddController {

	@Autowired
	private AutoreplyMessageListService autoreplyMessageListService;
	
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

	static final Logger logger = Logger.getLogger(MessageAddController.class);

	@GetMapping(path = "/messageadd/all")
	public @ResponseBody List<AutoreplyMessageList> getAllAutoreplyMessageList() {
		return autoreplyMessageListService.getAutoreplyMessageList();
	}

	@GetMapping(path = "/messageadd/{autoreplyID}")
	public @ResponseBody Map<String, Map<String, String>> getAllAutoreplyMessageListByID(@PathVariable("autoreplyID") String autoreplayid) {
		List<AutoreplyMessageList> list = autoreplyMessageListService.getAutoreplyMessageListByAutoreplyID(Long.parseLong(autoreplayid));
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
		StringBuilder memo = new StringBuilder();
		String id = "";
		Map<String, Map<String, String>> mappedList = new HashMap<>();
		if (!list.isEmpty()) {
			for (AutoreplyMessageList am : list) {
				id = String.valueOf(am.getId());
				msgType = am.getMessageType();
				if (am.getMessageType().equals(MessageType.TEXT.getValue())) {
					Optional<MessageText> obj = messageTextService.findById(am.getMessageId());
					if (obj.isPresent())
						txtmsg = obj.get().getText();
				}
				if (am.getMessageType().equals(MessageType.IMAGE.getValue())) {
					Optional<MessageImage> obj = messageImageService.findById(am.getMessageId());
					if (obj.isPresent()) {
						imageurl = obj.get().getOriginalContentUrl();
						previewurl = obj.get().getPreviewImageUrl();
					}
				}
				if (am.getMessageType().equals(MessageType.VIDEO.getValue())) {
					Optional<MessageVideo> obj = messageVideoService.findById(am.getMessageId());
					if (obj.isPresent()) {
						videourl = obj.get().getOriginalContentUrl();
						previewurl = obj.get().getPreviewImageUrl();
					}
				}
				if (am.getMessageType().equals(MessageType.AUDIO.getValue())) {
					Optional<MessageAudio> obj = messageAudioService.findById(am.getMessageId());
					if (obj.isPresent()) {
						audiourl = obj.get().getOriginalContentUrl();
						audioDuration = String.valueOf(obj.get().getDuration());
					}
				}
				if (am.getMessageType().equals(MessageType.STICKER.getValue())) {
					Optional<MessageSticker> obj = messageStickerService.findById(am.getMessageId());
					if (obj.isPresent()) {
						packageid = String.valueOf(obj.get().getPackageId());
						stickerid = obj.get().getStickerId();
					}
				}
				if (am.getMessageType().equals(MessageType.IMAGEMAP.getValue())) {
					Optional<MessageImageMap> obj = messageImageMapService.findById(am.getMessageId());
					if (obj.isPresent()) {
						imagemapid = String.valueOf(am.getMessageId());
						titleText = obj.get().getAltText();
						imageurl = obj.get().getBaseUrl();
					}
				}
				if (am.getMessageType().equals(MessageType.TEMPLATE.getValue())) {
					templateid = String.valueOf(am.getMessageId());
					Optional<MessageTemplate> obj = messageTemplateService.findById(am.getMessageId());
					if (obj.isPresent()) {
						titleText = obj.get().getTitle();
						txtmsg = obj.get().getText();
						imageurl = obj.get().getThumbnailImageUrl();
						templatetype = obj.get().getType();
						for (MessageTemplateAction d : obj.get().getMessageTemplateActionList()) {
							memo.append(d.getLabel() + ";");
						}
						stickerurl = obj.get().getText();
					}
				}
				JsonArray colobj = new JsonArray();
				List<MessageCarouselColumn> columnlist = new ArrayList<>();
				if (am.getMessageType().equals(MessageType.CAROUSEL.getValue())) {
					templateid = String.valueOf(am.getMessageId());
					Optional<MessageCarouselTemplate> obj = messageCarouselTemplateService.findById(am.getMessageId());
					if (obj.isPresent()) {
						titleText = obj.get().getAltText();
						imageurl = obj.get().getMessageCarouseColumnList().get(0).getThumbnailImageUrl();
						templatetype = obj.get().getType();
						for (MessageCarouselColumn col : obj.get().getMessageCarouseColumnList()) {
							columnlist.add(col);
							JsonObject c = new JsonObject();
							c.addProperty("text", col.getText());
							c.addProperty("imagurl", col.getThumbnailImageUrl());
							c.addProperty("title", col.getTitle());
							StringBuilder actString = new StringBuilder();
							for (MessageCarouselAction a : col.getMessageCarouselActionList())
								actString.append(a.getLabel() + "|");
							c.addProperty("memo", actString.toString());
							colobj.add(c);
						}
					}
				}
				String columnListString = colobj.toString();
				Map<String, String> mapped = new HashMap<>();
				mapped.put("id", id);
				mapped.put("type", msgType);
				mapped.put("txtmsg", txtmsg);
				mapped.put("imageurl", imageurl);
				mapped.put("previewur", previewurl);
				mapped.put("videourl", videourl);
				mapped.put("titleText", titleText);
				mapped.put("linkurl", linkurl);
				mapped.put("memo", memo.toString());
				mapped.put("audiourl", audiourl);
				mapped.put("audioDuration", audioDuration);
				mapped.put("imagemapid", imagemapid);
				mapped.put("templateid", templateid);
				mapped.put("templatetype", templatetype);
				mapped.put("stickerurl", stickerurl);
				mapped.put("packageid", packageid);
				mapped.put("stickerid", stickerid);
				mapped.put("columnList", columnListString);
				mapped.put("visibility", "true");
				mappedList.put("msg" + am.getOrderNum(), mapped);
			}
		}
		return mappedList;
	}
}
