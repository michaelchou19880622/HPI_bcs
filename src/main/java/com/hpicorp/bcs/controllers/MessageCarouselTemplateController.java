package com.hpicorp.bcs.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.MessageCarouselAction;
import com.hpicorp.bcs.entities.MessageCarouselColumn;
import com.hpicorp.bcs.entities.MessageCarouselTemplate;
import com.hpicorp.bcs.services.MessageCarouselTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageCarouselTemplateController {

	@Autowired
	private MessageCarouselTemplateService messageCarouselTemplateService;

	@GetMapping(path = "/messageCarouselTemplate/all")
	public @ResponseBody List<MessageCarouselTemplate> getAllMessageTemplate() {
		return messageCarouselTemplateService.getAllMessageCarouselTemplate();
	}

	@GetMapping(path = "/messageCarouselTemplate/{id}")
	public ResponseEntity<MessageCarouselTemplate> getAllMessageTemplateByID(@PathVariable long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Optional<MessageCarouselTemplate> messageImageMapOptional = messageCarouselTemplateService.findById(id);
		if (messageImageMapOptional.isPresent())
			return new ResponseEntity<>(messageImageMapOptional.get(), headers, HttpStatus.OK);
		else
			return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	@PostMapping(path = "/messageCarouselTemplate/new")
	public @ResponseBody String createMessageTemplate(@RequestBody MessageCarouselTemplate messageCarouselTemplate) {

		log.debug("messageCarouselTemplate count => {}", messageCarouselTemplate.getMessageCarouseColumnList().size());

		for (MessageCarouselColumn d : messageCarouselTemplate.getMessageCarouseColumnList()) {
			d.setMessageCarouselTemplate(messageCarouselTemplate);
			for (MessageCarouselAction col : d.getMessageCarouselActionList()) {
				col.setMessageCarouselColumn(d);
			}
		}
		messageCarouselTemplate.setModifyDatetime(new Date());
		messageCarouselTemplateService.insert(messageCarouselTemplate);
		return "Saved";
	}

	@DeleteMapping("/messageCarouselTemplate/{id}")
	public void deleteMessageImageMap(@PathVariable long id) {
		log.debug("deleteMessageImageMap:" + id);
		messageCarouselTemplateService.deleteById(id);
	}

	@PutMapping("/messageCarouselTemplate/{id}")
	public @ResponseBody String updateMessageCarouselTemplate(@RequestBody MessageCarouselTemplate messageCarouselTemplate, @PathVariable long id) {

		Optional<MessageCarouselTemplate> messageImageMapOptional = messageCarouselTemplateService.findById(id);

		if (!messageImageMapOptional.isPresent()) {
			return "Nodata [" + id + "]";
		}

		messageCarouselTemplate.setId(id);
		for (MessageCarouselColumn d : messageCarouselTemplate.getMessageCarouseColumnList()) {
			d.setMessageCarouselTemplate(messageCarouselTemplate);
			for (MessageCarouselAction col : d.getMessageCarouselActionList()) {
				col.setMessageCarouselColumn(d);
			}
		}
		messageCarouselTemplate.setModifyDatetime(new Date());
		messageCarouselTemplateService.save(messageCarouselTemplate);

		return "Saved";
	}

	@RequestMapping(path = "/getmessageCarouselTemplate/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> messageCarouselTemplateByID(@PathVariable long id) throws IOException {
		Optional<MessageCarouselTemplate> messageImageMapOptional = messageCarouselTemplateService.findById(id);

		if (messageImageMapOptional.isPresent()) {
			String urlString = messageImageMapOptional.get().getMessageCarouseColumnList().get(0).getThumbnailImageUrl();

			BufferedImage image = null;
			try {
				URL url = new URL(urlString);
				image = ImageIO.read(url);
			} catch (IOException e) {
				log.error("error => ", e);
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", bos);
			byte[] bytes = bos.toByteArray();

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} else
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(null);
	}
}
