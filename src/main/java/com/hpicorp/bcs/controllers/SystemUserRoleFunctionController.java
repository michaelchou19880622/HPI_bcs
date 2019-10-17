package com.hpicorp.bcs.controllers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.repositories.SystemUserRoleFunctionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userrolefunction")
public class SystemUserRoleFunctionController {
	
	@Autowired
	SystemUserRoleFunctionRepository systemUserRoleFunctionRepository;
	
	/**
	 * [Read List] 系統使用者可使用的function
	 * @param roleId
	 * @return
	 */
	@GetMapping("/roleid/{roleid}")
	public List<String> getSystemUserRoleFunctionByRoleId(@PathVariable(value = "roleid") String roleId) {
		return systemUserRoleFunctionRepository.findRoleFunctionList(roleId);
	}
	
}
