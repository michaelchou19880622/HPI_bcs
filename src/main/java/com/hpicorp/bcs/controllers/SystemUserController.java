package com.hpicorp.bcs.controllers;


import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.SystemUser;
import com.hpicorp.bcs.entities.dto.ApiResponse;
import com.hpicorp.bcs.repositories.SystemUserRepository;
import com.hpicorp.bcs.repositories.SystemUserRoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class SystemUserController {
	
	@Autowired
	SystemUserRepository systemUserRepository;
	
	@Autowired
    SystemUserRoleRepository systemUserRoleRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@GetMapping("")
	public Page<SystemUser> getAllSystemUser(@PageableDefault(value = 10) Pageable pageable) {
	    return systemUserRepository.findAll(pageable);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/new")
	public ResponseEntity<Object> createSystemUser(@Valid @RequestBody SystemUser systemUser) {
		if(!systemUser.getAccount().matches("[a-zA-Z0-9|\\.]*")) {
			return new ResponseEntity(new ApiResponse(false, "Invalid Account"), HttpStatus.BAD_REQUEST);
		}		
		if(systemUserRepository.existsByAccount(systemUser.getAccount())) {
			return new ResponseEntity(new ApiResponse(false, "Account is already taken!"), HttpStatus.BAD_REQUEST);
        }
		systemUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        systemUser.setStatus("0");
        systemUser.setCreateTime(new Date());
        systemUser.setModifyTime(new Date());
        systemUser.setRoles(systemUser.getRoles());
        
	    systemUserRepository.save(systemUser);
		return ResponseEntity.ok().build();
	}
	
	// Get a SystemUser
	@GetMapping("/acc/id/{id}")
	public SystemUser getSystemUserById(@PathVariable(value = "id") Long systemUserId) throws Exception {
		return systemUserRepository.findById(systemUserId)
	            .orElseThrow(() -> new Exception("SystemUser error => " + systemUserId));
	}
	
	// Update a SystemUser
	@PutMapping("/{id}")
	public SystemUser updateSystemUser(@PathVariable(value = "id") Long systemUserId, @Valid @RequestBody SystemUser systemUserDetails) throws Exception {

	    SystemUser systemUser = systemUserRepository.findById(systemUserId)
	            .orElseThrow(() -> new Exception("SystemUser error => " + systemUserId));

	    systemUser.setName(systemUserDetails.getName());
	    systemUser.setPassword(passwordEncoder.encode(systemUserDetails.getPassword()));
	    systemUser.setStatus(systemUserDetails.getStatus());
	    systemUser.setLineuserUid(systemUserDetails.getLineuserUid());
	    systemUser.setModifyTime(new Date());
	    systemUser.setModifyAccount(systemUserDetails.getAccount());
	    systemUser.setRoles(systemUserDetails.getRoles());

	    return systemUserRepository.save(systemUser);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteSystemUser(@PathVariable(value = "id") Long systemUserId) throws Exception {
		SystemUser systemUser = systemUserRepository.findById(systemUserId)
	            .orElseThrow(() -> new Exception("SystemUser error => " + systemUserId));
		systemUserRepository.delete(systemUser);
	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("/acc/{account}")
	public SystemUser getSystemUserByAccount(@PathVariable(value = "account") String account) throws Exception {
		return systemUserRepository.findByAccount(account)
	            .orElseThrow(() -> new Exception("SystemUser error =>" + account));
	}
	
	
	
	
	
	
}
