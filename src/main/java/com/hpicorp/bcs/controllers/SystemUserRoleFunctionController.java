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

import com.hpicorp.bcs.entities.SystemUserRoleFunction;
import com.hpicorp.bcs.repositories.SystemUserRoleFunctionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userrolefunction")
public class SystemUserRoleFunctionController {
	
	@Autowired
	SystemUserRoleFunctionRepository systemUserRoleFunctionRepository;
	
	@GetMapping("")
	public List<SystemUserRoleFunction> getAllSystemUserRoleFunction() {
	    return systemUserRoleFunctionRepository.findAll();
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> createSystemUserRoleFunction(@Valid @RequestBody SystemUserRoleFunction systemUserRoleFunction) {
		systemUserRoleFunctionRepository.save(systemUserRoleFunction);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/id/{id}")
	public SystemUserRoleFunction getSystemUserRoleFunctionById(@PathVariable(value = "id") Long systemUserRoleFunctionId) throws Exception {
		return systemUserRoleFunctionRepository.findById(systemUserRoleFunctionId)
	            .orElseThrow(() -> new Exception("SystemUserRoleFunction error => " + systemUserRoleFunctionId));
	}
	
	@PutMapping("/{id}")
	public SystemUserRoleFunction updateSystemUserRoleFunction(@PathVariable(value = "id") Long systemUserRoleFunctionId,
	                                        @Valid @RequestBody SystemUserRoleFunction systemUserRoleFunctionDetails) throws Exception {

	    SystemUserRoleFunction systemUserRoleFunction = systemUserRoleFunctionRepository.findById(systemUserRoleFunctionId)
	            .orElseThrow(() -> new Exception("SystemUserRoleFunction error => " + systemUserRoleFunctionId));

	    systemUserRoleFunction.setFunctionId(systemUserRoleFunctionDetails.getFunctionId());
	    systemUserRoleFunction.setSystemUserRoleId(systemUserRoleFunctionDetails.getSystemUserRoleId());

	    SystemUserRoleFunction updatedSystemUserRoleFunction = systemUserRoleFunctionRepository.save(systemUserRoleFunction);
	    return updatedSystemUserRoleFunction;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSystemUserRoleFunction(@PathVariable(value = "id") Long systemUserRoleFunctionId) throws Exception {
		SystemUserRoleFunction systemUserRoleFunction = systemUserRoleFunctionRepository.findById(systemUserRoleFunctionId)
	            .orElseThrow(() -> new Exception("SystemUserRoleFunction error => " + systemUserRoleFunctionId));
		systemUserRoleFunctionRepository.delete(systemUserRoleFunction);
	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("/roleid/{roleid}")
	public List<String> getSystemUserRoleFunctionByRoleId(@PathVariable(value = "roleid") String roleId) {
		return systemUserRoleFunctionRepository.findRoleFunctionList(roleId);
	}
	
}
