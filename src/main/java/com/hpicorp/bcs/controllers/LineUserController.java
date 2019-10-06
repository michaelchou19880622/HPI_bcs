package com.hpicorp.bcs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.repositories.LineUserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/lineuser")
public class LineUserController {

	@Autowired
	private LineUserRepository lineUserRepository;

	@GetMapping()
	public List<LineUser> getAllLineUser() {
		return lineUserRepository.findAll();
	}

	@PostMapping()
	public LineUser createLineUser(@Valid @RequestBody LineUser lineUser) {
		return lineUserRepository.save(lineUser);
	}

}
