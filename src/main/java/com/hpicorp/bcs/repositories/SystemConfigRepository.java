package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hpicorp.bcs.entities.SystemConfig;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
	
}
