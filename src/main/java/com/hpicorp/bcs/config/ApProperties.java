package com.hpicorp.bcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ApProperties {
	
	@Value("${ap.endpoint}")
	private String endpoint;
	
	@Value("${ap.secret}")
    private String secret;

	@Value("${line.channel.id}")
	private String lineChannelId;
	
}
