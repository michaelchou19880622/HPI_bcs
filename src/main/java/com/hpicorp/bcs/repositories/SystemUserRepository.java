package com.hpicorp.bcs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hpicorp.bcs.entities.SystemUser;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

	public Boolean existsByName(String name);

	public Boolean existsByAccount(String account);
	
	public Optional<SystemUser> findByAccount(String account);

	public Optional<SystemUser> findByNameOrAccount(String name, String account);

	public Optional<SystemUser> findByName(String name);

	public List<SystemUser> findByIdIn(List<Long> ids);

	public Page<SystemUser> findAll(Pageable pageable);

}
