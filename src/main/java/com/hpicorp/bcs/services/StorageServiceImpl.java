package com.hpicorp.bcs.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.config.StorageConfig;
import com.hpicorp.bcs.storage.StorageService;
import com.hpicorp.core.exception.StorageException;

@Service
public class StorageServiceImpl implements StorageService {
	
	private final Path path;
	
	@Autowired
    public StorageServiceImpl(StorageConfig config) {
        this.path = Paths.get(config.getLocation());
    }

	@Override
	public void init() {
		try {
            Files.createDirectories(path);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage => ", e);
        }
	}

}
