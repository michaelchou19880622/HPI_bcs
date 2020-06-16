package com.hpicorp.bcs.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.services.RichMenuService;
import com.hpicorp.core.entities.RichMenu;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/richmenu")
public class RichMenuController {
	
	@Autowired
	private RichMenuService richMenuService;
	
	/**
	 * 取得 RichMenu 列表
	 */
	@GetMapping("/list")
	public ResponseEntity<Object> getRichMenuList() {
		
		List<Map<String, Object>> listMapObject;
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
	    
		try {
			listMapObject = this.richMenuService.findRichmenuListByLevel();
			
		} catch (Exception e) {
			log.error("get rich menu list error = {}", e.getMessage());
			
			return ResponseEntity.ok()
								 .headers(headers)
								 .body(e.getMessage());
		}
		return ResponseEntity.ok()
							 .headers(headers)
							 .body(listMapObject);
	}

	@PostMapping("/list/id")
	public ResponseEntity<Object> getRichMenuListById(@RequestBody RichMenu richMenu) {
		log.info("richMenu = {}", richMenu);
		
		Object obj;
		try {
			obj = this.richMenuService.getRichMenuListById(richMenu);
		} catch (Exception e) {
			log.error("get rich menu list error = {}", e.getMessage());
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().body(obj);
 	}
	
	@GetMapping("/delete/all/from/line")
	public void deleteAllRichMenuFromLine() throws IOException {
		richMenuService.deleteAllRichMenuFromLine();
	}
	
	/**
	 * 創建 RichMenu 
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createRichMenu(@RequestBody List<RichMenu> richMenuList, HttpServletRequest req ) {
		log.info("richMenuList = {}", richMenuList);
		
		try {
			log.info("req.getScheme() = {}", req.getScheme());
			log.info("req.getServerName() = {}", req.getServerName());
			log.info("req.getServerPort() = {}", req.getServerPort());
			
			String originLocation = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
			log.info("Origin Location = {}", originLocation);
			
			this.richMenuService.createRichMenuList(richMenuList, originLocation);
		} catch (Exception e) {
			log.error("create rich menu error = {}", e);
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改 RichMenu 
	 */
	@PostMapping("/update")
	public ResponseEntity<Object> updateRichMenu(@RequestBody List<RichMenu> richMenuList, HttpServletRequest req) {
		log.info("richMenuList = {}", richMenuList);
		
		try {
			log.info("req.getScheme() = {}", req.getScheme());
			log.info("req.getServerName() = {}", req.getServerName());
			log.info("req.getServerPort() = {}", req.getServerPort());
			
			String originLocation = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
			log.info("Origin Location = {}", originLocation);
			
			this.richMenuService.updateRichMenuList(richMenuList, originLocation);
		} catch (Exception e) {
			log.error("update rich menu error = {}", e);
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 綁定 用戶 與 RichMenu 
	 
	@PostMapping("/link/{lineUserId}/{richMenuId}")
	public ResponseEntity<Object> getRichMenuIdByUser(
			@PathVariable("lineUserId") String lineUserId, @PathVariable("richMenuId") String richMenuId) {
		try {
			this.richMenuService.setRichMenuByUser(lineUserId, richMenuId, "");
		} catch (Exception e) {
			log.error("get rich menu by user error = {}", e.getMessage());
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().body("綁定成功");
	}

	/**
	 * 刪除綁定 用戶 與 RichMenu 
	 */
	@DeleteMapping("/link/{lineUserId}")
	public ResponseEntity<Object> deleteLinkRichMenuByUser(@PathVariable("lineUserId") String lineUserId) {
		try {
			this.richMenuService.deleteLinkRichMenuByUser(lineUserId);
		} catch (Exception e) {
			log.error("delete link rich menu error = {}", e.getMessage());
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().body("刪除綁定成功");
	}
	
	@DeleteMapping()
	public ResponseEntity<Object> delete(@RequestBody RichMenu richMenu) {
		try {
			this.richMenuService.deleteRichMenu(richMenu);
		} catch (Exception e) {
			log.error("delete richmenu error => {}", e.getMessage());
			return ResponseEntity.ok().body(e.getMessage());
		}
		return ResponseEntity.ok().body("刪除圖文選單成功");
	}
	
}
