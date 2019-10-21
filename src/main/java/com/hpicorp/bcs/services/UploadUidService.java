package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hpicorp.core.entities.UploadUid;
import com.hpicorp.core.repository.UploadUidRepository;

@Service
public class UploadUidService {

	@Autowired
	private UploadUidRepository uploadUidRepository;
	
	public boolean existsByFilename(String filename) {
		return uploadUidRepository.existsByFilename(filename);
	}
	
	public boolean checkUidValid() {
		int invalidCount = uploadUidRepository.checkUidValid().intValue();
		return invalidCount == 0;
	}
	
	public List<UploadUid> findByfilename(String filename) {
		return uploadUidRepository.findByFilename(filename);
	}
	
	public List<UploadUid> findByGroupId(Long groupId) {
		return uploadUidRepository.findByGroupId(groupId);
	}
	
	public List<UploadUid> findByGroupIdAndFilename(Long groupId, String filename) {
		return uploadUidRepository.findByGroupIdAndFilename(groupId, filename);
	}
	
	public ResponseEntity<Object> deleteByFilename(String filename) {
		uploadUidRepository.deleteByFilename(filename);
		return ResponseEntity.ok().build();
	}
	
	public ResponseEntity<Object> updateByFilename(String filename, Long groupId) {
		uploadUidRepository.updateByFilename(filename, groupId);
		return ResponseEntity.ok().build();
	}
	
	public List<Map<String, String>> getFilenameAndOriginalFilenameByGroupId(Long groupId) {
		return uploadUidRepository.getFilenameAndOriginalFilenameByGroupId(groupId);
	}
	
	public void saveUploadUidByList(List<UploadUid> list) {
		uploadUidRepository.saveAll(list);
	}
}
