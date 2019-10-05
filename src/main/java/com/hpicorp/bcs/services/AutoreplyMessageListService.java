package com.hpicorp.bcs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.AutoreplyMessageList;
import com.hpicorp.bcs.repositories.AutoreplyMessageListRepository;

@Service
public class AutoreplyMessageListService {

	@Autowired
	private AutoreplyMessageListRepository autoreplyMessageListRepository ;
	
	public List<AutoreplyMessageList> getAutoreplyMessageList() {
		return autoreplyMessageListRepository.findAll();
	}
	
	public List<AutoreplyMessageList> getAutoreplyMessageListByAutoreplyID(long id) {
		return autoreplyMessageListRepository.getAutoreplyMessageListByAutoreplyID(id);
	}
	
	public void insert(AutoreplyMessageList autoreplyMessageList) {
		autoreplyMessageListRepository.save(autoreplyMessageList);
	}
	
	public void deleteById(long id) {
		autoreplyMessageListRepository.deleteById(id);
	}
	
	public void deleteByAutoreplyId(long id) {
		autoreplyMessageListRepository.deleteByAutoreplyID(id);
	}	
	
}
