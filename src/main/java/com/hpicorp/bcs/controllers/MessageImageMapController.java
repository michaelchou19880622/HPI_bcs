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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import com.hpicorp.bcs.entities.MessageImageMap;
import com.hpicorp.bcs.entities.MessageImageMapAction;
import com.hpicorp.bcs.services.MessageImageMapService;

import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class MessageImageMapController {
	
	@Autowired
	private MessageImageMapService messageImageMapService;
	
	@GetMapping(path = "/messageImageMap")
	public String index() {
		return "Index Page";
	}
	
	@GetMapping(path = "/messageImageMap/all")
	public @ResponseBody Page<MessageImageMap> getAllMessageImageMap(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable){
		return messageImageMapService.getAllMessageImageMap(pageable);		
	}
	
	@GetMapping(path = "/messageImageMap/all/{type}")
	public @ResponseBody List<MessageImageMap> getAllMessageImageMap(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String type){
		return messageImageMapService.getAllMessageImageMapByType(type);
	}	
	
	@RequestMapping(path = "/getImageMap/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]>  getMessageImageMapByID(@PathVariable Long id) throws IOException {
		Optional<MessageImageMap> messageImageMapOptional = messageImageMapService.findById(id);
		String urlString =  messageImageMapOptional.get().getBaseUrl().replace("?id=123", "");
		log.info("urlString = {}", urlString);
		
		String urlExtension = urlString.substring(urlString.lastIndexOf(".") + 1);
		log.info("urlExtension = {}", urlExtension);

		MediaType mediaType = urlExtension.equalsIgnoreCase("png")? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
		
		BufferedImage image = null;
        try {
            URL url = new URL(urlString);
            image = ImageIO.read(url);
        } catch (IOException e) {
        		e.printStackTrace();
        }
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		String srcType = (mediaType == MediaType.IMAGE_PNG)? "png" : "jpg";
		
		ImageIO.write(image, srcType, bos);
		byte[] bytes = bos.toByteArray();	  
		
		return ResponseEntity.ok().contentType(mediaType).body(bytes);
	}
	
	@PostMapping(path = "/messageImageMap/new") 
	public @ResponseBody String createMessageImageMap(@RequestBody MessageImageMap messageImageMap) {
		for(MessageImageMapAction d : messageImageMap.getMessageImageMapActionList()) {
			d.setMessageImageMap(messageImageMap);
		}
		messageImageMap.setBaseUrl(messageImageMap.getBaseUrl() + "?id=123");
//		messageImageMap.setBaseUrl(messageImageMap.getBaseUrl());
		messageImageMap.setModifyTime(new Date());
		messageImageMapService.insert(messageImageMap);
		return "Saved";
	}
	
	@DeleteMapping("/messageImageMap/{id}")
	public void deleteMessageImageMap(@PathVariable Long id) {
		messageImageMapService.deleteById(id);
	}
	
	@PutMapping("/messageImageMap/{id}")
	public @ResponseBody String updateMessageImageMap(@RequestBody MessageImageMap messageImageMap, @PathVariable Long id) {
		Optional<MessageImageMap> messageImageMapOptional = messageImageMapService.findById(id);
		if (!messageImageMapOptional.isPresent()) {
			return "No data [" + id + "]";
		}		
		messageImageMap.setId(id);
		for(MessageImageMapAction d : messageImageMap.getMessageImageMapActionList()) {
			d.setMessageImageMap(messageImageMap);
		}
		messageImageMap.setModifyTime(new Date());
		messageImageMapService.save(messageImageMap);
		return "Saved";
	}	
}
