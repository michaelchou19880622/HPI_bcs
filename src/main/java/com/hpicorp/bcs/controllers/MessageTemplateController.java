package com.hpicorp.bcs.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.MessageCarouselAction;
import com.hpicorp.bcs.entities.MessageCarouselColumn;
import com.hpicorp.bcs.entities.MessageCarouselTemplate;
import com.hpicorp.bcs.entities.MessageTemplate;
import com.hpicorp.bcs.entities.MessageTemplateAction;
import com.hpicorp.bcs.services.MessageCarouselTemplateService;
import com.hpicorp.bcs.services.MessageTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageTemplateController {
	
	@Autowired
	private MessageTemplateService messageTemplateService;
	
	@Autowired
	private MessageCarouselTemplateService messageCarouselTemplateService;

	@RequestMapping(path = "/getMessageTemplate/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getMessageImageMapByID(@PathVariable Integer id) throws IOException {
		Optional<MessageTemplate> messageTemplateOptional = messageTemplateService.findById(id);

		BufferedImage image = null;
		String extension = "";
		if (messageTemplateOptional.isPresent()) {
			String urlString = messageTemplateOptional.get().getThumbnailImageUrl();
			try {
				URL url = new URL(urlString);
				image = ImageIO.read(url);
				extension = urlString.substring(urlString.length() - 3, urlString.length());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		ImageIO.write(image, extension, bos);
		byte[] bytes = bos.toByteArray();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
	}

	@GetMapping(path = "/messageTemplate/allData")
	public @ResponseBody List<MessageTemplate> getAllMessageTemplates(@RequestParam(value = "page", defaultValue = "0") Integer page) {
		List<MessageTemplate> tmpList = messageTemplateService.getAllMessageTemplate();
		List<MessageCarouselTemplate> ctmplist = messageCarouselTemplateService.getAllMessageCarouselTemplate();
		for (MessageCarouselTemplate t : ctmplist) {
			MessageTemplate newtemplate = new MessageTemplate();
			newtemplate.setId(t.getId().intValue());
			newtemplate.setAltText(t.getAltText());
			newtemplate.setType(t.getType());
			MessageCarouselColumn col = t.getMessageCarouseColumnList().get(0);
			newtemplate.setThumbnailImageUrl(col.getThumbnailImageUrl());
			List<MessageTemplateAction> alist = new ArrayList<>();
			for (MessageCarouselAction a : col.getMessageCarouselActionList()) {
				MessageTemplateAction newList = new MessageTemplateAction();
				newList.setData(a.getData());
				newList.setId(a.getId());
				newList.setLabel(a.getLabel());
				newList.setTemplateType(a.getTemplateType());
				newList.setText(a.getText());
				newList.setType(a.getType());
				newList.setUri(a.getUri());
				alist.add(newList);
			}
			newtemplate.setMessageTemplateActionList(alist);
			tmpList.add(newtemplate);
		}
		Collections.sort(tmpList, new Comparator<MessageTemplate>() {
			public int compare(MessageTemplate o1, MessageTemplate o2) {
				if (o1.getId() > o2.getId())
					return -1;
				return o1.getId() == o2.getId() ? 0 : 1;
			}
		});

		return tmpList;
	}

	@GetMapping(path = "/messageTemplate/all")
	public @ResponseBody Map<String, Object> getAllMessageTemplate(@RequestParam(value = "page", defaultValue = "0") Integer page) {

		List<MessageTemplate> tmpList = messageTemplateService.getAllMessageTemplate();
		List<MessageCarouselTemplate> ctmplist = messageCarouselTemplateService.getAllMessageCarouselTemplate();
		for (MessageCarouselTemplate t : ctmplist) {
			MessageTemplate newtemplate = new MessageTemplate();
			newtemplate.setId(t.getId().intValue());
			newtemplate.setAltText(t.getAltText());
			newtemplate.setType(t.getType());
			MessageCarouselColumn col = t.getMessageCarouseColumnList().get(0);
			newtemplate.setThumbnailImageUrl(col.getThumbnailImageUrl());
			List<MessageTemplateAction> alist = new ArrayList<>();
			for (MessageCarouselAction a : col.getMessageCarouselActionList()) {
				MessageTemplateAction newList = new MessageTemplateAction();
				newList.setData(a.getData());
				newList.setId(a.getId());
				newList.setLabel(a.getLabel());
				newList.setTemplateType(a.getTemplateType());
				newList.setText(a.getText());
				newList.setType(a.getType());
				newList.setUri(a.getUri());
				alist.add(newList);
			}
			newtemplate.setMessageTemplateActionList(alist);
			tmpList.add(newtemplate);
		}
		Collections.sort(tmpList, new Comparator<MessageTemplate>() {
			public int compare(MessageTemplate o1, MessageTemplate o2) {
				if (o1.getId() > o2.getId())
					return -1;
				return o1.getId() == o2.getId() ? 0 : 1;
			}
		});
		int ifrom = 0;
		int ito = 10;
		if (page > 0) {
			ifrom = page * 10;
			ito = ifrom + 10;
		}
		if (ito > tmpList.size())
			ito = tmpList.size();

		Map<String, Object> result = new HashMap<>();
		result.put("total", tmpList.size() / 10 + (tmpList.size() % 10 > 0 ? 1 : 0));
		result.put("number", page);
		result.put("totalElements", tmpList.size());
		result.put("content", tmpList.subList(ifrom, ito));
		return result;
	}

	// 20180703 Arnor, To get message id, change return type from String to
	// MessageTemplate
	@PostMapping(path = "/messageTemplate/new")
	public @ResponseBody MessageTemplate createMessageTemplate(@RequestBody MessageTemplate messageTemplate) {
		for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
			d.setMessageTemplate(messageTemplate);
		}
		messageTemplate.setModifyDatetime(new Date());
		return messageTemplateService.insert(messageTemplate);
	}

	@DeleteMapping("/messageTemplate/{id}")
	public void deleteMessageImageMap(@PathVariable long id) {
		messageTemplateService.deleteById(id);
	}

	@PutMapping("/messageTemplate/{id}")
	public @ResponseBody String updateMessageImageMap(@RequestBody MessageTemplate messageTemplate, @PathVariable Integer id) {
		Optional<MessageTemplate> messageImageMapOptional = messageTemplateService.findById(id);
		if (!messageImageMapOptional.isPresent()) {
			return "Nodata [" + id + "]";
		}
		messageTemplate.setId(id);
		for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
			d.setMessageTemplate(messageTemplate);
		}
		messageTemplate.setModifyDatetime(new Date());
		messageTemplateService.save(messageTemplate);
		return "Saved";
	}

	// 20180806 Arnor, build Apply agreement confirm message and apply completed
	// confirm message
	@SuppressWarnings("unchecked")
	public MessageTemplate buildEventConfirmMessage(Map<String, Object> confirmMessageMap) {
		MessageTemplate messageTemplate = new MessageTemplate();
		MessageTemplateAction messageTemplateAction;

		messageTemplate.setType(confirmMessageMap.get("type").toString());
		messageTemplate.setAltText(confirmMessageMap.get("alt_text").toString());
		messageTemplate.setThumbnailImageUrl(confirmMessageMap.get("thumbnail_image_url").toString());
		messageTemplate.setImageAspectRatio(confirmMessageMap.get("image_aspect_ratio").toString());
		messageTemplate.setImageSize(confirmMessageMap.get("image_size").toString());
		messageTemplate.setImageBackgroundColor(confirmMessageMap.get("image_background_color").toString());
		messageTemplate.setTitle(confirmMessageMap.get("title").toString());
		messageTemplate.setText(confirmMessageMap.get("text").toString());
		List<MessageTemplateAction> messageTemplateActionList = new ArrayList<>();
		for (Map<String, String> action : (List<Map<String, String>>) confirmMessageMap.get("messageTemplateActionList")) {
			messageTemplateAction = new MessageTemplateAction();
			messageTemplateAction.setTemplateType(action.get("template_type"));
			messageTemplateAction.setType(action.get("type"));
			messageTemplateAction.setLabel(action.get("label"));
			messageTemplateAction.setData(action.get("data"));
			messageTemplateAction.setText(action.get("text"));
			messageTemplateAction.setUri(action.get("uri"));
			messageTemplateAction.setMessageTemplate(messageTemplate);
			messageTemplateActionList.add(messageTemplateAction);
		}
		messageTemplate.setMessageTemplateActionList(messageTemplateActionList);
		return messageTemplate;
	}
	
}
