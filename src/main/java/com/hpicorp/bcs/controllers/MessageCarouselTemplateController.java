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

import com.hpicorp.bcs.services.MessageCarouselTemplateService;
import com.hpicorp.core.entities.MessageCarouselAction;
import com.hpicorp.core.entities.MessageCarouselColumn;
import com.hpicorp.core.entities.MessageCarouselTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageCarouselTemplateController {

	@Autowired
	private MessageCarouselTemplateService messageCarouselTemplateService;

	/**
	 * [Read List]旋轉樣板列表
	 * @param pageable
	 * @return
	 */
	@GetMapping(path = "/messageCarouselTemplate/all")
	public Page<MessageCarouselTemplate> getAllMessageTemplate(@PageableDefault(size = 10) Pageable pageable) {
		return messageCarouselTemplateService.getAllMessageCarouselTemplate(pageable);
	}

	/**
	 * [Create]建立旋轉樣板
	 * @param messageCarouselTemplate
	 * @return
	 */
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
	
	/**
	 * [Update]編輯旋轉樣板訊息
	 * @param messageCarouselTemplate 旋轉樣板訊息
	 * @return
	 */
	@PostMapping("/messageCarouselTemplate/update/{id}")
	public ResponseEntity<String> updateMessageCarouselTemplate(@RequestBody MessageCarouselTemplate messageCarouselTemplate, 
			@PathVariable(value="id") Long id) {

		Optional<MessageCarouselTemplate> originalMessageCarouselTemplateOptional = messageCarouselTemplateService.findById(id);

		if (!originalMessageCarouselTemplateOptional.isPresent()) {
			return ResponseEntity.badRequest().body("查無資料");
		}
		// 檢查防呆
		boolean check = this.messageCarouselTemplateService.checkCarouselTemplate(messageCarouselTemplate);
		log.info("更新旋轉樣板檢查：" + check);
		if(check) {
			try {
				// 刪掉原本的columns
				List<MessageCarouselColumn> originalColumns = originalMessageCarouselTemplateOptional.get().getMessageCarouseColumnList();
				originalMessageCarouselTemplateOptional.get().setMessageCarouseColumnList(null);
				this.messageCarouselTemplateService.save(originalMessageCarouselTemplateOptional.get());
				this.messageCarouselTemplateService.removeColumns(originalColumns);
				// 建新的column
				log.info("updateMessageImageMap: {}", id);
				messageCarouselTemplate.setId(originalMessageCarouselTemplateOptional.get().getId());
				for(MessageCarouselColumn d : messageCarouselTemplate.getMessageCarouseColumnList()) {
					log.info("updateMessageImageMap getId: {}", d.getId());
					d.setMessageCarouselTemplate(messageCarouselTemplate);
				    
					for(MessageCarouselAction col : d.getMessageCarouselActionList()) {
						col.setMessageCarouselColumn(d);
					}				
				}
				messageCarouselTemplate.setModifyDatetime(new Date());
				messageCarouselTemplateService.save(messageCarouselTemplate);
			} catch (Exception e) {
				log.info(e.toString());
				log.info("更新旋轉樣板失敗: {}", e.getMessage());
				return ResponseEntity.badRequest().body("更新失敗");
			}
			return ResponseEntity.ok().body("更新成功");
		}
		return ResponseEntity.badRequest().body("更新失敗 - 資料錯誤");
		
	}

	/**
	 * [Delete]刪除旋轉樣板
	 * @param id
	 */
	@DeleteMapping("/messageCarouselTemplate/{id}")
	public void deleteMessageImageMap(@PathVariable long id) {
		log.debug("deleteMessageImageMap:" + id);
		messageCarouselTemplateService.deleteById(id);
	}

	/**
	 *	訊息列表顯示旋轉樣板第一個Column的圖片
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/getmessageCarouselTemplate/{id}")
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
