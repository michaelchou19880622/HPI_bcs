package com.hpicorp.bcs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.SystemUser;
import com.hpicorp.bcs.repositories.SystemUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String account) {
		log.info("『 登入者 』=> {}", account);
		SystemUser systemUser = systemUserRepository.findByAccount(account)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with account : " + account));
		return UserPrincipal.create(systemUser);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		SystemUser user = systemUserRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
		return UserPrincipal.create(user);
	}

}
