package com.hpicorp.bcs.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.repositories.LineUserRepository;

@Service
public class LineUserService {

	@Autowired
	private LineUserRepository lineUserRepository;
	
	public Optional<LineUser> findById(Long id) {
		return lineUserRepository.findById(id);
	}
	
	public Optional<LineUser> findByUid(String uid) {
		return lineUserRepository.findByLineUid(uid);
	}
	
	public Long getIDByLINEUID(String uid) {
		Long id = lineUserRepository.getIdByUid(uid);
		id = id == null ? 0L :id;
		return id;
	}
	
	
	
}
