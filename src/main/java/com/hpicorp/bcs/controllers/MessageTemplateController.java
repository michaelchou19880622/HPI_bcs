package com.hpicorp.bcs.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.services.MessageCarouselTemplateService;
import com.hpicorp.bcs.services.MessageTemplateService;
import com.hpicorp.core.entities.MessageCarouselAction;
import com.hpicorp.core.entities.MessageCarouselColumn;
import com.hpicorp.core.entities.MessageCarouselTemplate;
import com.hpicorp.core.entities.MessageTemplate;
import com.hpicorp.core.entities.MessageTemplateAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class MessageTemplateController {
	
	@Autowired
	private MessageTemplateService messageTemplateService;
	
	@Autowired
	private MessageCarouselTemplateService messageCarouselTemplateService;

	/**
	 *	列表讀取樣板訊息圖片API
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/getMessageTemplate/{id}")
	public ResponseEntity<byte[]> getMessageImageMapByID(@PathVariable Long id) throws IOException {
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

	/**
	 * [Read]顯示所有樣板訊息清單(預計要改成Page)
	 * @param page
	 * @return
	 */
	@GetMapping(path = "/messageTemplate/allData")
	public List<MessageTemplate> getAllMessageTemplates(@RequestParam(value = "page", defaultValue = "0") Integer page) {
		List<MessageTemplate> tmpList = messageTemplateService.getAllMessageTemplateByType();
		List<MessageCarouselTemplate> ctmplist = messageCarouselTemplateService.getAllMessageCarouselTemplate();
		for (MessageCarouselTemplate t : ctmplist) {
			MessageTemplate newtemplate = new MessageTemplate();
			newtemplate.setId(t.getId());
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

	/**
	 * [Read List]樣板訊息列表
	 * @param pageable 分頁
	 * @return
	 */
	@GetMapping(path = "/messageTemplate/all")
	public Page<MessageTemplate> getAllMessageTemplate(@PageableDefault(size = 10) Pageable pageable) {
		return messageTemplateService.getAllMessageTemplateByType(pageable);
	}

	/**
	 * [Create]建立樣板訊息
	 * @param messageTemplate
	 * @return
	 */
	@PostMapping(path = "/messageTemplate/new")
	public MessageTemplate createMessageTemplate(@RequestBody MessageTemplate messageTemplate) {
		for (MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
			d.setMessageTemplate(messageTemplate);
		}
		messageTemplate.setModifyDatetime(new Date());
		return messageTemplateService.insert(messageTemplate);
	}
	
	/**
	 * [Update]編輯樣板訊息
	 * @param template 樣板訊息
	 * @return
	 */
	@PostMapping("/messageTemplate/update/{id}")
	public ResponseEntity<String> updateMessageTemplate(@RequestBody MessageTemplate messageTemplate, 
			@PathVariable(value="id") Long id) {
		
		Optional<MessageTemplate> originalMessageTemplateOptional = messageTemplateService.findById(id);
		if (!originalMessageTemplateOptional.isPresent()) {
			return ResponseEntity.badRequest().body("查無資料");
		}
		boolean check = this.messageTemplateService.checkTemplate(messageTemplate);
		log.info("更新樣板檢查：" + check);
		if(check) {
			try {
				// 刪掉原本的Actions
				List<MessageTemplateAction> originalActionList = originalMessageTemplateOptional.get().getMessageTemplateActionList();
				originalMessageTemplateOptional.get().setMessageTemplateActionList(null);
				this.messageTemplateService.save(originalMessageTemplateOptional.get());
				this.messageTemplateService.removeActions(originalActionList);
				
				// 建新的Actions
				messageTemplate.setId(id);
				for(MessageTemplateAction d : messageTemplate.getMessageTemplateActionList()) {
					log.debug("updateMessageImageMap getId:" + d.getId());
					d.setMessageTemplate(messageTemplate);
				}
				messageTemplate.setModifyDatetime(new Date());
				messageTemplateService.save(messageTemplate);
				return ResponseEntity.ok().body("更新成功");
			} catch (Exception e) {
				log.error("更新樣板失敗- Error= {}", e);
				return ResponseEntity.badRequest().body("更新失敗");
			}
		}
		return ResponseEntity.badRequest().body("更新失敗 - 資料錯誤");

	}

	/**
	 * [Delete]刪除樣板訊息
	 * @param id
	 */
	@DeleteMapping("/messageTemplate/{id}")
	public void deleteMessageImageMap(@PathVariable long id) {
		messageTemplateService.deleteById(id);
	}
	
}
