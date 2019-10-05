package com.hpicorp.bcs.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.MessageAudio;
import com.hpicorp.bcs.entities.MessageImage;
import com.hpicorp.bcs.entities.MessageSticker;
import com.hpicorp.bcs.entities.MessageText;
import com.hpicorp.bcs.entities.MessageVideo;
import com.hpicorp.bcs.repositories.MessageAudioRepository;
import com.hpicorp.bcs.repositories.MessageImageRepository;
import com.hpicorp.bcs.repositories.MessageStickerRepository;
import com.hpicorp.bcs.repositories.MessageTextRepository;
import com.hpicorp.bcs.repositories.MessageVideoRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageTextRepository messageTextRepository;
	
	@Autowired
	private MessageStickerRepository messageStickerRepository;
	
	@Autowired
	private MessageVideoRepository messageVideoRepository;
	
	@Autowired
	private MessageAudioRepository messageAudioRepository;
	
	@Autowired
	private MessageImageRepository messageImageRepository;

	@GetMapping("/text/id/{id}")
	public MessageText getTextMessageById(@PathVariable(value = "id") Long messgaeId) throws Exception {
		return messageTextRepository.findById(messgaeId)
				.orElseThrow(() -> new Exception("get MessageText error => " + messgaeId));
	}

	@GetMapping("/sticker/id/{id}")
	public MessageSticker getStickerMessageById(@PathVariable(value = "id") Long messgaeId) throws Exception {
		return messageStickerRepository.findById(messgaeId)
				.orElseThrow(() -> new Exception("get MessageSticker error => " + messgaeId));
	}

	@GetMapping("/video/id/{id}")
	public MessageVideo getVideoMessageById(@PathVariable(value = "id") Long messgaeId) throws Exception {
		return messageVideoRepository.findById(messgaeId)
				.orElseThrow(() -> new Exception("get MessageVideo error => " + messgaeId));
	}

	@GetMapping("/audio/id/{id}")
	public MessageAudio getAudioMessageById(@PathVariable(value = "id") Long messgaeId) throws Exception {
		return messageAudioRepository.findById(messgaeId)
				.orElseThrow(() -> new Exception("get MessageAudio error => " + messgaeId));
	}

	@GetMapping("/image/id/{id}")
	public MessageImage getImageMessageById(@PathVariable(value = "id") Long messgaeId) throws Exception {
		return messageImageRepository.findById(messgaeId)
				.orElseThrow(() -> new Exception("get MessageImage => " + messgaeId));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/new/text")
	public ResponseEntity<Object> createTextMessage(@Valid @RequestBody MessageText messageText) {
		MessageText createdMessageText = messageTextRepository.save(messageText);
		return new ResponseEntity(createdMessageText, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/new/sticker")
	public ResponseEntity<Object> createStickerMessage(@Valid @RequestBody MessageSticker messageSticker) {
		MessageSticker createdMessageSticker = messageStickerRepository.save(messageSticker);
		return new ResponseEntity(createdMessageSticker, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/new/video")
	public ResponseEntity<Object> createVideoMessage(@Valid @RequestBody MessageVideo messageVideo) {
		MessageVideo createdMessageViedo = messageVideoRepository.save(messageVideo);
		return new ResponseEntity(createdMessageViedo, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/new/audio")
	public ResponseEntity<Object> createAudioMessage(@Valid @RequestBody MessageAudio messageAudio) {
		MessageAudio createdMessageAudio = messageAudioRepository.save(messageAudio);
		return new ResponseEntity(createdMessageAudio, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/new/image")
	public ResponseEntity<Object> createImageMessage(@Valid @RequestBody MessageImage messageImage) {
		MessageImage createdMessageImage = messageImageRepository.save(messageImage);
		return new ResponseEntity(createdMessageImage, HttpStatus.OK);
	}

	@PutMapping("/text/{id}")
	public MessageText updateTextMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody MessageText messageTextDetails) throws Exception {
		MessageText messageText = messageTextRepository.findById(messageId)
				.orElseThrow(() -> new Exception("update MessageText => " + messageId));
		messageText.setText(messageTextDetails.getText());
		return messageTextRepository.save(messageText);
	}

	@PutMapping("/sticker/{id}")
	public MessageSticker updateStickerMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody MessageSticker messageStickerDetails) throws Exception {
		MessageSticker messageSticker = messageStickerRepository.findById(messageId)
				.orElseThrow(() -> new Exception("update MessageSticker => " + messageId));
		messageSticker.setPackageId(messageStickerDetails.getPackageId());
		messageSticker.setStickerId(messageStickerDetails.getStickerId());
		return messageStickerRepository.save(messageSticker);
	}

	@PutMapping("/video/{id}")
	public MessageVideo updateVideoMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody MessageVideo messageVideoDetails) throws Exception {
		MessageVideo messageVideo = messageVideoRepository.findById(messageId)
				.orElseThrow(() -> new Exception("update MessageVideo => " + messageId));
		messageVideo.setOriginalContentUrl(messageVideoDetails.getOriginalContentUrl());
		messageVideo.setPreviewImageUrl(messageVideoDetails.getPreviewImageUrl());
		return messageVideoRepository.save(messageVideo);
	}

	@PutMapping("/audio/{id}")
	public MessageAudio updateAudioMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody MessageAudio messageAudioDetails) throws Exception {
		MessageAudio messageAudio = messageAudioRepository.findById(messageId)
				.orElseThrow(() -> new Exception("update MessageAudio => " + messageId));
		messageAudio.setOriginalContentUrl(messageAudioDetails.getOriginalContentUrl());
		messageAudio.setDuration(messageAudioDetails.getDuration());
		return messageAudioRepository.save(messageAudio);
	}

	@PutMapping("/image/{id}")
	public MessageImage updateImageMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody MessageImage messageImageDetails) throws Exception {
		MessageImage messageImage = messageImageRepository.findById(messageId)
				.orElseThrow(() -> new Exception("update MessageImage => " + messageId));
		messageImage.setOriginalContentUrl(messageImageDetails.getOriginalContentUrl());
		messageImage.setPreviewImageUrl(messageImageDetails.getPreviewImageUrl());
		return messageImageRepository.save(messageImage);
	}

	@DeleteMapping("/text/{id}")
	public ResponseEntity<Object> deleteTextMessage(@PathVariable(value = "id") Long messageId) throws Exception {
		MessageText messageText = messageTextRepository.findById(messageId)
				.orElseThrow(() -> new Exception("delete MessageText => " + messageId));
		messageTextRepository.delete(messageText);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/sticker/{id}")
	public ResponseEntity<Object> deleteStickerMessage(@PathVariable(value = "id") Long messageId) throws Exception {
		MessageSticker messageSticker = messageStickerRepository.findById(messageId)
				.orElseThrow(() -> new Exception("delete MessageSticker => " + messageId));
		messageStickerRepository.delete(messageSticker);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/video/{id}")
	public ResponseEntity<Object> deleteVideoMessage(@PathVariable(value = "id") Long messageId) throws Exception {
		MessageVideo messageVideo = messageVideoRepository.findById(messageId)
				.orElseThrow(() -> new Exception("delete MessageVideo => " + messageId));
		messageVideoRepository.delete(messageVideo);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/audio/{id}")
	public ResponseEntity<Object> deleteAudioMessage(@PathVariable(value = "id") Long messageId) throws Exception {
		MessageAudio messageAudio = messageAudioRepository.findById(messageId)
				.orElseThrow(() -> new Exception("delete MessageAudio => " + messageId));
		messageAudioRepository.delete(messageAudio);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/image/{id}")
	public ResponseEntity<Object> deleteImageMessage(@PathVariable(value = "id") Long messageId) throws Exception {
		MessageImage messageImage = messageImageRepository.findById(messageId)
				.orElseThrow(() -> new Exception("delete MessageImage => " + messageId));
		messageImageRepository.delete(messageImage);
		return ResponseEntity.ok().build();
	}

}
