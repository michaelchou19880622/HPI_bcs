package com.hpicorp.bcs.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hpicorp.bcs.entities.SystemUser;
import com.hpicorp.bcs.entities.SystemUserRole;
import com.hpicorp.bcs.entities.dto.ApiResponse;
import com.hpicorp.bcs.entities.dto.JwtAuthenticationResponse;
import com.hpicorp.bcs.entities.dto.LoginRequest;
import com.hpicorp.bcs.entities.dto.SignUpRequest;
import com.hpicorp.bcs.enums.RoleName;
import com.hpicorp.bcs.exception.AppException;
import com.hpicorp.bcs.repositories.SystemUserRepository;
import com.hpicorp.bcs.repositories.SystemUserRoleRepository;
import com.hpicorp.bcs.security.JwtTokenProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Autowired
	private SystemUserRoleRepository systemUserRoleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getAccount(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (systemUserRepository.existsByAccount(signUpRequest.getAccount())) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Account is already taken!"));
		}

		// Creating user's account
		SystemUser systemUser = new SystemUser(signUpRequest.getAccount(), signUpRequest.getName(), signUpRequest.getPassword());
		systemUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));
		systemUser.setStatus("0");
		systemUser.setCreateTime(new Date());
		systemUser.setCreateAccount(signUpRequest.getName());
		systemUser.setModifyTime(new Date());
		systemUser.setModifyAccount(signUpRequest.getName());

		SystemUserRole systemUserRole = systemUserRoleRepository.findById(RoleName.ROLE000002.toString())
				.orElseThrow(() -> new AppException("User Role not set."));

		systemUser.setRoles(Collections.singleton(systemUserRole));

		SystemUser result = systemUserRepository.save(systemUser);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/acc/{account}")
				.buildAndExpand(result.getAccount()).toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
	
}