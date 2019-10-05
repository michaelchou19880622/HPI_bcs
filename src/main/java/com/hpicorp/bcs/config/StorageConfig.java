package com.hpicorp.bcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("storage")
public class StorageConfig {

	@Value("${upload.file.directory}")
    private String location;
	
}
