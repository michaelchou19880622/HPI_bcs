package com.hpicorp.bcs.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.hpicorp.bcs.entities.Autoreply;
import com.hpicorp.bcs.entities.AutoreplyDetail;
import com.hpicorp.bcs.entities.AutoreplyMessageList;
import com.hpicorp.bcs.entities.MessageAudio;
import com.hpicorp.bcs.entities.MessageImage;
import com.hpicorp.bcs.entities.MessageSticker;
import com.hpicorp.bcs.entities.MessageTemplate;
import com.hpicorp.bcs.entities.MessageTemplateAction;
import com.hpicorp.bcs.entities.MessageText;
import com.hpicorp.bcs.entities.MessageVideo;
import com.hpicorp.bcs.enums.MessageType;
import com.hpicorp.bcs.services.AutoreplyMessageListService;
import com.hpicorp.bcs.services.AutoreplyService;
import com.hpicorp.bcs.services.MessageAudioService;
import com.hpicorp.bcs.services.MessageImageService;
import com.hpicorp.bcs.services.MessageStickerService;
import com.hpicorp.bcs.services.MessageTemplateService;
import com.hpicorp.bcs.services.MessageTextService;
import com.hpicorp.bcs.services.MessageVideoService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class AutoreplyController {

	private static final String STATUS = "status";

	@Autowired
	private AutoreplyService autoreplyService;

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
	private MessageTemplateService messageTemplateService;

	@GetMapping(value = "/autoreply/all")
	public @ResponseBody Page<Autoreply> getAllAutoreply(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
		return autoreplyService.getAutoreplyListWithoutDefault(pageable);
	}

	@GetMapping(path = "/autoreply/period/{key}")
	public @ResponseBody Page<Autoreply> getAllSeAutoreplyByPeriod(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("key") String key) {
		Date date = new Date();
		if (key.equals("InActive")) {
			return autoreplyService.getAutoreplyByInActivePeriod(pageable, date);
		} else if (key.equals("Expired")) {
			return autoreplyService.getAutoreplyByExpiredPeriod(pageable, date);
		} else {
			return autoreplyService.getAutoreplyByActivePeriod(pageable, date);
		}
	}

	@DeleteMapping("/autoreply/{id}")
	public void deleteAutoreply(@PathVariable long id) {
		autoreplyMessageListService.deleteByAutoreplyId(id);
		autoreplyService.deleteById(id);
	}

	@PostMapping(path = "/autoreply/new")
	public @ResponseBody Map<String, String> createAutoreply(@RequestBody Autoreply autoreply) {

		for (AutoreplyDetail d : autoreply.getAutoreplyDetails()) {
			d.setAutoreply(autoreply);
		}

		try {
			for (AutoreplyMessageList am : autoreply.getAutoreplyMessageList()) {
				am.setAutoreply(autoreply);
				String[] typelist = am.getMessageType().split(";");
				if (typelist[0].trim().equals(MessageType.TEXT.getValue())) {
					MessageText txt = am.getMessageTextList().get(0);
					messageTextService.insert(txt);
					am.setMessageId(txt.getId());
				} else if (typelist[0].trim().equals(MessageType.IMAGE.getValue())) {
					MessageImage img = am.getMessageImageList().get(0);
					messageImageService.insert(img);
					am.setMessageId(img.getId());
				} else if (typelist[0].trim().equals(MessageType.VIDEO.getValue())) {
					MessageVideo video = am.getMessageVideoList().get(0);
					messageVideoService.insert(video);
					am.setMessageId(video.getId());
				} else if (typelist[0].trim().equals(MessageType.AUDIO.getValue())) {
					MessageAudio audio = am.getMessageAudioList().get(0);
					messageAudioService.insert(audio);
					am.setMessageId(audio.getId());
				} else if (typelist[0].trim().equals(MessageType.STICKER.getValue())) {
					MessageSticker sticker = am.getMessageStickerList().get(0);
					messageStickerService.insert(sticker);
					am.setMessageId(sticker.getId());
				} else if (typelist[0].trim().equals(MessageType.LINK.getValue())) {
					MessageTemplate messageTemplate = am.getMessageTemplateList().get(0);
					for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
						d.setMessageTemplate(messageTemplate);
					}
					messageTemplateService.insert(messageTemplate);
					am.setMessageId(messageTemplate.getId().longValue());
					typelist[0] = MessageType.TEMPLATE.getValue();
				}
				am.setOrderNum(Integer.parseInt(typelist[1], 10));
				am.setMessageType(typelist[0].trim());
			}
			autoreply.setModificationTime(new Date());

			if (autoreply.getCreationTime() == null) {
				autoreply.setCreationTime(new Date());
			}

			autoreplyService.insert(autoreply);

			Map<String, String> mapped = new HashMap<>();
			mapped.put(STATUS, "Success");
			mapped.put("id", autoreply.getId().toString());
			return mapped;
		} catch (Exception e) {
			log.info("Exception = ", e);
			return null;
		}
		
	}

	@PutMapping("/autoreply/{id}")
	public @ResponseBody Map<String, String> updateAutoreply(@RequestBody Autoreply autoreply, @PathVariable long id) {
		Optional<Autoreply> autoreplyMapOptional = autoreplyService.findById(id);
		Map<String, String> mapped = new HashMap<>();
		if (!autoreplyMapOptional.isPresent()) {
			mapped.put(STATUS, "Failure");
			return mapped;
		}

		List<AutoreplyMessageList> list = autoreplyMessageListService.getAutoreplyMessageListByAutoreplyID(id);
		deleteUnused(id, autoreply, list);
		autoreply.setId(id);

		for (AutoreplyDetail d : autoreply.getAutoreplyDetails())
			d.setAutoreply(autoreply);

		for (AutoreplyMessageList am : autoreply.getAutoreplyMessageList()) {
			am.setAutoreply(autoreply);
			String[] typelist = am.getMessageType().split(";");
			boolean runUpdate = true;

			processByMessageType(typelist[0].trim(), am);
			if (typelist[0].trim().equals(MessageType.TEMPLATE.getValue())) {
				for (AutoreplyMessageList am1 : list) {
					if (am1.getId() == am.getId() && am1.getMessageId() == am.getMessageId())
						runUpdate = false;
				}
			}
			am.setOrderNum(Integer.parseInt(typelist[1], 10));
			if (runUpdate)
				autoreplyMessageListService.insert(am);
		}
		autoreply.setModificationTime(new Date());
		autoreplyService.save(autoreply);

		mapped.put(STATUS, "Success");
		mapped.put("id", autoreply.getId().toString());
		return mapped;
	}

	@PutMapping("/autoreply/status/{id}/{status}")
	public @ResponseBody Map<String, String> updateAutoreplyStatus(@PathVariable long id, @PathVariable String status) {

		Optional<Autoreply> autoreplyMapOptional = autoreplyService.findById(id);
		Map<String, String> mapped = new HashMap<>();
		if (!autoreplyMapOptional.isPresent()) {
			mapped.put(STATUS, "Failure");
			return mapped;
		}
		autoreplyMapOptional.get().setStatus(status);
		autoreplyMapOptional.get().setModificationTime(new Date());
		autoreplyService.save(autoreplyMapOptional.get());

		mapped.put(STATUS, "Success");
		mapped.put("id", autoreplyMapOptional.get().getId().toString());
		return mapped;
	}

	private void deleteUnused(long id, Autoreply autoreply, List<AutoreplyMessageList> list) {

		for (AutoreplyMessageList am1 : list) {
			boolean isExist = false;
			for (AutoreplyMessageList am2 : autoreply.getAutoreplyMessageList()) {
				if (am1.getId() == am2.getId())
					isExist = true;
			}
			if (!isExist) {
				autoreplyMessageListService.deleteById(am1.getId());
			}
		}
		autoreplyService.deleteAutoreplyDetailByAutoreplyID(id);
	}

	private void processByMessageType(String msgtype, AutoreplyMessageList am) {
		if (msgtype.equals(MessageType.TEXT.getValue())) {
			MessageText txt = am.getMessageTextList().get(0);
			messageTextService.insert(txt);
			am.setMessageId(txt.getId());
		} else if (msgtype.trim().equals(MessageType.IMAGE.getValue())) {
			MessageImage img = am.getMessageImageList().get(0);
			messageImageService.insert(img);
			am.setMessageId(img.getId());
		} else if (msgtype.trim().equals(MessageType.VIDEO.getValue())) {
			MessageVideo video = am.getMessageVideoList().get(0);
			messageVideoService.insert(video);
			am.setMessageId(video.getId());
		} else if (msgtype.trim().equals(MessageType.AUDIO.getValue())) {
			MessageAudio audio = am.getMessageAudioList().get(0);
			messageAudioService.insert(audio);
			am.setMessageId(audio.getId());
		} else if (msgtype.trim().equals(MessageType.STICKER.getValue())) {
			MessageSticker sticker = am.getMessageStickerList().get(0);
			messageStickerService.insert(sticker);
			am.setMessageId(sticker.getId());
		} else if (msgtype.trim().equals(MessageType.LINK.getValue())) {
			MessageTemplate messageTemplate = am.getMessageTemplateList().get(0);
			for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
				d.setMessageTemplate(messageTemplate);
			}
			messageTemplateService.insert(messageTemplate);
			am.setMessageId(messageTemplate.getId().longValue());
			msgtype = MessageType.TEMPLATE.getValue();
		}

		am.setMessageType(msgtype.trim());
	}

}
