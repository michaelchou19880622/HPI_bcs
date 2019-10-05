package com.hpicorp.bcs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpicorp.bcs.entities.SystemConfig;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

	public Optional<SystemConfig> findByConfigKey(String configKey);
	
}
