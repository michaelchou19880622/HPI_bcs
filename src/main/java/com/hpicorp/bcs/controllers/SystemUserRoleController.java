package com.hpicorp.bcs.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.core.entities.SystemUserRole;
import com.hpicorp.core.repository.SystemUserRoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/role")
public class SystemUserRoleController {
	
	@Autowired
	SystemUserRoleRepository systemUserRoleRepository;
	
	/**
	 * [Read List] 系統使用者腳色下拉選單
	 * @return
	 */
	@GetMapping("")
	public List<SystemUserRole> getAllSystemUserRole() {
	    return systemUserRoleRepository.findAll();
	}
}
