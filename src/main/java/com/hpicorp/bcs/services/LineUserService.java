package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
	
	public Optional<LineUser> findByShareLineUserBySPrivateCode(String sPrivateCode) {
		return this.lineUserRepository.findBySPrivateCode(sPrivateCode);
	}
	
	public String findBySPrivateCode(String sprivateCode) {
		Optional<LineUser> lineUser = this.lineUserRepository.findBySPrivateCode(sprivateCode);
		return lineUser.isPresent() ? lineUser.get().getLineUid() : null;
	}
	
	public String findByPrivateCode(String privateCode) {
		Optional<LineUser> lineUser = this.lineUserRepository.findByPrivateCode(privateCode);
		return lineUser.isPresent() ? lineUser.get().getLineUid() : null;
	}
	
	/**
	 * MGM 結合活動
	 */
	public String getSPrivateCode(String lineUid) {
		int x = 0;
		// Step 1. 查詢出該用戶資訊
		Optional<LineUser> lineUser = this.findByUid(lineUid);
		if (lineUser.isPresent()) {
			LineUser lu = lineUser.get();
			// Step 1-1. 判斷 privateCode or sPrivateCode 是否存在
			if (null == lu.getPrivateCode() || "".equals(lu.getPrivateCode())) {
				lu.setPrivateCode(this.createRandomCode());
				x += 1;
			}
			if (null == lu.getSPrivateCode() || "".equals(lu.getSPrivateCode())) {
				lu.setSPrivateCode(this.createRandomCode());
				x += 1;
			}
			// Step 1-2. 如果 x > 0 在進行保存
			if (x > 0) {
				this.lineUserRepository.save(lu);
			}
			return lu.getSPrivateCode();
		}
		return null;
	}
	
	/**
	 * 亂數產生隨機碼，我故意洋裝成類似 LineUid 的 33 碼  
	 */
	private String createRandomCode() {
		return "U" + UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
