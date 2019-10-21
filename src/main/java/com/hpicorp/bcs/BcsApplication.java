package com.hpicorp.bcs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hpicorp.bcs.config.StorageConfig;
import com.hpicorp.bcs.storage.StorageService;

@EnableJpaRepositories(basePackages = { "com.hpicorp.core.repository" })
@EntityScan(basePackages = { "com.hpicorp.core.entities" })
@ComponentScan(basePackages = { "com.hpicorp.core.*", "com.hpicorp.bcs.*" })
@SpringBootApplication
@EnableConfigurationProperties(StorageConfig.class)
public class BcsApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BcsApplication.class);
    }	
	
	public static void main(String[] args) {
		SpringApplication.run(BcsApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner init(StorageService storageService) {
		return args -> {
            storageService.init();
        };
    }
	
}
