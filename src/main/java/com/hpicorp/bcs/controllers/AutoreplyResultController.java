package com.hpicorp.bcs.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.common.DateTimeModel;
import com.hpicorp.bcs.entities.dto.AutoreplyResultBody;
import com.hpicorp.bcs.services.AutoreplyResultModelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/result/reply/autoreply")
public class AutoreplyResultController {

	@Autowired
	private AutoreplyResultModelService model;

	@RequestMapping(path = "/keyword", method = RequestMethod.GET)
	public ResponseEntity<Object> getByAutoreplyResultBody(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "period", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date period) {
		if (page == null) {
			page = 0;
		}
		try {
			period = period == null ? DateTimeModel.initialDate() : period;
			AutoreplyResultBody body = new AutoreplyResultBody(keyword, period, page);
			Map<String, Object> content = model.getByAutoreplyResultBody(body);
			return ResponseEntity.ok(content);
		} catch (Exception e) {
			log.error("getByAutoreplyResultBody => {}", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
