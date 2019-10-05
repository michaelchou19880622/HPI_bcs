package com.hpicorp.bcs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.SystemUserRole;
import com.hpicorp.bcs.repositories.SystemUserRoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/role")
public class SystemUserRoleController {
	
	@Autowired
	SystemUserRoleRepository systemUserRoleRepository;
	
	@GetMapping("")
	public List<SystemUserRole> getAllSystemUserRole() {
	    return systemUserRoleRepository.findAll();
	}
	
	@PostMapping("")
	public SystemUserRole createSystemUserRole(@Valid @RequestBody SystemUserRole systemUserRole) {
	    return systemUserRoleRepository.save(systemUserRole);
	}
	
	@GetMapping("/{id}")
	public SystemUserRole getSystemUserRoleById(@PathVariable(value = "id") String systemUserRoleId) throws Exception {
		return systemUserRoleRepository.findById(systemUserRoleId)
	            .orElseThrow(() -> new Exception("SystemUserRole error => " + systemUserRoleId));
	} 
	
	@PutMapping("/{id}")
	public SystemUserRole updateSystemUserRole(@PathVariable(value = "id") String systemUserRoleId, @Valid @RequestBody SystemUserRole systemUserRoleDetails) throws Exception {
	    SystemUserRole systemUserRole = systemUserRoleRepository.findById(systemUserRoleId)
	            .orElseThrow(() -> new Exception("SystemUserRole error => " + systemUserRoleId));
	    systemUserRole.setName(systemUserRoleDetails.getName());
	    return systemUserRoleRepository.save(systemUserRole);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteSystemUserRole(@PathVariable(value = "id") String systemUserRoleId) throws Exception {
		SystemUserRole systemUserRole = systemUserRoleRepository.findById(systemUserRoleId)
	            .orElseThrow(() -> new Exception("SystemUserRole error => " + systemUserRoleId));
		systemUserRoleRepository.delete(systemUserRole);
	    return ResponseEntity.ok().build();
	}
}
