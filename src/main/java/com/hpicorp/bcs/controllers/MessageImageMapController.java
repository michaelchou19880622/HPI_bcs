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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.services.MessageImageMapService;
import com.hpicorp.core.entities.MessageImageMap;
import com.hpicorp.core.entities.MessageImageMapAction;

import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class MessageImageMapController {
	
	@Autowired
	private MessageImageMapService messageImageMapService;
	
	
	/**
	 * [Read List]圖文訊息列表
	 * @param pageable
	 * @return
	 */
	@GetMapping(path = "/messageImageMap/all")
	public Page<MessageImageMap> getAllMessageImageMap(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable){
		return messageImageMapService.getAllMessageImageMap(pageable);		
	}
	
	/**
	 *	取得所有圖文訊息清單
	 * @param pageable
	 * @param type
	 * @return
	 */
	@GetMapping(path = "/messageImageMap/all/{type}")
	public @ResponseBody List<MessageImageMap> getAllMessageImageMap(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String type){
		return messageImageMapService.getAllMessageImageMapByType(type);
	}	
	
	/**
	 *	訊息列表顯示圖文訊息圖片
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/getImageMap/{id}")
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
	
	/**
	 * [Create]建立圖文訊息
	 * @param messageImageMap
	 * @return
	 */
	@PostMapping("/messageImageMap/new") 
	public ResponseEntity<String> createMessageImageMap(@RequestBody MessageImageMap messageImageMap) {
		boolean check = this.messageImageMapService.checkImageMap(messageImageMap);
		if(check) {
			try {
				for(MessageImageMapAction d : messageImageMap.getMessageImageMapActionList()) {
					d.setMessageImageMap(messageImageMap);
				}
				messageImageMap.setModifyTime(new Date());
				messageImageMapService.insert(messageImageMap);
				return ResponseEntity.ok().body("新增成功");
			} catch (Exception e) {
				log.info("新增圖文訊息失敗： {}", e.getMessage());
				return ResponseEntity.badRequest().body("新增失敗");
			}
		}
		return ResponseEntity.badRequest().body("新增失敗-資料錯誤");
		
	}
	
	/**
	 * [Delete]刪除圖文訊息
	 * @param id
	 */
	@DeleteMapping("/messageImageMap/{id}")
	public void deleteMessageImageMap(@PathVariable Long id) {
		messageImageMapService.deleteById(id);
	}
	
	/**
	 * [Update]編輯圖文訊息
	 * @param messageImageMap
	 * @param id
	 * @return
	 */
	@PostMapping("/messageImageMap/update/{id}")
	public ResponseEntity<String> updateMessageImageMap(@RequestBody MessageImageMap messageImageMap,
			@PathVariable(value="id") Long id) {
		Optional<MessageImageMap> originalMessageImageMapOptional = messageImageMapService.findById(id);
		if (!originalMessageImageMapOptional.isPresent()) {
			return ResponseEntity.badRequest().body("查無資料");
		}
		boolean check = this.messageImageMapService.checkImageMap(messageImageMap);
		if(check) {
			try {
				// 清除舊的actions
				List<MessageImageMapAction> originalActionList = originalMessageImageMapOptional.get().getMessageImageMapActionList();
				originalMessageImageMapOptional.get().setMessageImageMapActionList(null);
				this.messageImageMapService.save(originalMessageImageMapOptional.get());
				this.messageImageMapService.removeActions(originalActionList);
				
				messageImageMap.setId(id);
				for(MessageImageMapAction d : messageImageMap.getMessageImageMapActionList()) {
					d.setMessageImageMap(messageImageMap);
				}
				messageImageMap.setModifyTime(new Date());
				messageImageMapService.save(messageImageMap);
				
				return ResponseEntity.ok().body("更新成功");
			} catch (Exception e) {
				log.info("更新失敗", e.getMessage());
				return ResponseEntity.badRequest().body("更新失敗");
			}
		}
		return ResponseEntity.badRequest().body("更新失敗-資料錯誤");
	}
}
