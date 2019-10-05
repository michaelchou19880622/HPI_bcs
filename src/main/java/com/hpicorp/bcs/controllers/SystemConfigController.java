package com.hpicorp.bcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.services.SystemConfigService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/systemconfig")
public class SystemConfigController {

	@Autowired
	private SystemConfigService systemConfigService;
	
	@GetMapping
	public ResponseEntity<Object> getSystemConfig() {
		return ResponseEntity.ok().body(systemConfigService.getSystemConfig());
	}
	
}
