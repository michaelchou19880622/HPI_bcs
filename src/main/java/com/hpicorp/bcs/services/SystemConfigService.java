package com.hpicorp.bcs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.SystemConfig;
import com.hpicorp.bcs.exception.AppException;
import com.hpicorp.bcs.repositories.SystemConfigRepository;

@Service
public class SystemConfigService {

	@Autowired
	private SystemConfigRepository systemConfigRepository;
	
	@Value("${line.config.key}")
	private String lineConfigKey;

	public String getSystemConfig() {
		Optional<SystemConfig> systemConfig = this.systemConfigRepository.findByConfigKey(lineConfigKey);
		if (!systemConfig.isPresent()) {
			throw new AppException("該 Config key 不存在");
		}
		return systemConfig.get().getConfigValue();
	}
	
}
