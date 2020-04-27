package com.hpicorp.bcs.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hpicorp.bcs.services.AzureCDNService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FileUploadController {

	@Autowired
	private AzureCDNService azureCDNService;
	
    @PostMapping("/fileupload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
    	    	
		// 基本檢查
		String newfname = StringUtils.cleanPath(file.getOriginalFilename());
		if (file.isEmpty())
			throw new Exception("Failed to store empty file " + newfname);
		
        if (newfname.contains(".."))
            throw new Exception("Cannot store file with relative path outside current directory " + newfname);
    	
		// 開始重組新的檔名
		int lastIndexOfDot = newfname.lastIndexOf('.');
		String fileExtension = null;
		
		if (lastIndexOfDot > 0) {
			fileExtension = newfname.substring(lastIndexOfDot + 1);
		}
		
        newfname = UUID.randomUUID().toString() + "." + fileExtension; 
        
		// 開始上傳圖片
		try {
			newfname = azureCDNService.uploadFile(file, newfname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success:" + newfname;
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleStorageFileNotFound(Exception exc) {
        return ResponseEntity.notFound().build();
    }    
   
}
