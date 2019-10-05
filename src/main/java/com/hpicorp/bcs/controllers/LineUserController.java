package com.hpicorp.bcs.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.entities.UploadUid;
import com.hpicorp.bcs.repositories.LineUserRepository;
import com.hpicorp.bcs.services.UploadUidService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/lineuser")
public class LineUserController {
	
	@Autowired
	private UploadUidService uploadUidService;

	@Autowired
	private LineUserRepository lineUserRepository;

	@GetMapping()
	public List<LineUser> getAllLineUser() {
		return lineUserRepository.findAll();
	}

	@PostMapping()
	public LineUser createLineUser(@Valid @RequestBody LineUser lineUser) {
		return lineUserRepository.save(lineUser);
	}

	@PostMapping("/compare")
	public ResponseEntity<Object> compareLineUser(@Valid @RequestBody List<Map<String, String>> fileList) {
		List<String> mappedUidList = new ArrayList<>();
		if (fileList.isEmpty()) {
			return ResponseEntity.badRequest().body("尚未上傳檔案！");
		} else {
			for (Map<String, String> filenameMap : fileList) {
				String filename = filenameMap.get("response").split("success:")[1];
				List<UploadUid> uploadUidList = uploadUidService.findByfilename(filename);
				for (UploadUid uploadUid : uploadUidList) {
					if (!mappedUidList.contains(uploadUid.getUid()))
						mappedUidList.add(uploadUid.getUid());
				}
			}
		}
		return ResponseEntity.ok().body(mappedUidList.size());
	}

}
