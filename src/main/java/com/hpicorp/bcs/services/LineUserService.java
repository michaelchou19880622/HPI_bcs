package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.repositories.LineUserRepository;

@Service
public class LineUserService {

	@Autowired
	private LineUserRepository lineUserRepository;
	
	public List<LineUser> getAllLineUser() {
		return lineUserRepository.findAll();
	}
		
	public void insert(LineUser lineUser) {
		lineUserRepository.save(lineUser);
	}
	
	public LineUser save(LineUser lineUser) {
		return lineUserRepository.save(lineUser);		
	}
	
	public Optional<LineUser> findById(long id) {
		return lineUserRepository.findById(id);
	}
	
	public Optional<LineUser> findByUid(String uid) {
		return lineUserRepository.findByLineUid(uid);
	}
	
	public void deleteById(long id) {
		lineUserRepository.deleteById(id);
	}	
	
	public Long getIDByLINEUID(String uid) {
		Long id = lineUserRepository.getIdByUid(uid);
		id = id == null ? 0L :id;
		return id;
	}
	
	
	
}
