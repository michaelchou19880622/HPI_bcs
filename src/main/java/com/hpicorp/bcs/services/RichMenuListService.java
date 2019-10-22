package com.hpicorp.bcs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.RichMenuList;
import com.hpicorp.core.repository.RichMenuListRepository;

@Service
public class RichMenuListService {

	@Autowired
	private RichMenuListRepository richMenuListRepository;

	public RichMenuList findByRichMenuId(String richMenuId) {
		Optional<RichMenuList> richMenuListOptional = richMenuListRepository.findByRichMenuId(richMenuId);
		return richMenuListOptional.isPresent() ? richMenuListOptional.get() : null; 
	}
	
	public RichMenuList save(RichMenuList richMenuList) {
		return this.richMenuListRepository.save(richMenuList);
	}
	
	public void delete(RichMenuList richMenuList) {
		richMenuListRepository.delete(richMenuList);
	}
	
}
