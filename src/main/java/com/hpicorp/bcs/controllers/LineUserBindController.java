package com.hpicorp.bcs.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.services.LineUserBindingService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LineUserBindController {
	
	@Autowired
	private LineUserBindingService lineUserBindingService;
	
	/**
	 * Update a LineUser bind status
	 * @param ChannelId
	 * @param updateModel
	 * @return
	 */
	@PostMapping("/userStatusUpdate/{ChannelId}")
	public ResponseEntity<Object> updateLineUser(@PathVariable(value = "ChannelId") String ChannelId, @Valid @RequestBody String updateModel) {
	    return lineUserBindingService.bindLineUserAndSetUserTrackStatus(ChannelId, updateModel);
	}
		
}
