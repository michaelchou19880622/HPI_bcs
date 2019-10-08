package com.hpicorp.bcs.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.entities.dto.ApiResponse;
import com.hpicorp.bcs.enums.LineUserBindStatus;
import com.hpicorp.bcs.enums.LineUserStatus;
import com.hpicorp.bcs.enums.LineUserTrackSource;
import com.hpicorp.bcs.repositories.LineUserRepository;
import com.hpicorp.bcs.services.LineUserService;
import com.hpicorp.bcs.services.UserTrackService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LineUserBindController {
	
	private static final Logger logger = LoggerFactory.getLogger(LineUserBindController.class);
	
	@Autowired
	private LineUserService lineUserService;
	
	@Autowired
	private UserTrackService userTrackService;

	@Autowired
	private com.hpicorp.bcs.config.ApProperties properties;
	
	@Autowired
	LineUserRepository lineUserRepository;
	
	// TODO: 到時候要把邏輯的部分搬到service裡面去做 --- Michael 20191008
	// Update a LineUser bind status
	@PostMapping("/userStatusUpdate/{ChannelId}")
	public ResponseEntity<?> updateLineUser(@PathVariable(value = "ChannelId") String ChannelId, @Valid @RequestBody String updateModel) {
		
		logger.info("updateLineUser : ChannelId = {}", ChannelId);
		logger.info("updateLineUser : RequestBody = {}", updateModel);
		
		try {
			// Step 1. Check if ChanelID is correct? If 'not' -> return error code, message, if 'yes' -> Step 2.
			logger.info("lineChannelId = " + properties.getLineChannelId());
			
			if (!ChannelId.equals(properties.getLineChannelId())) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Line channel id not match."));
			}
			
			// Step 2. Parse request body to json string and map to UserBindModel
			Map<String, String> tmpMap = new HashMap<String, String>();
			ObjectMapper objMapper = new ObjectMapper();

			tmpMap = objMapper.readValue(updateModel, new TypeReference<HashMap<String, String>>() {});
			logger.info("tmpMap = " + tmpMap);

			tmpMap.put("status", tmpMap.get("bind_status"));
			tmpMap.put("time", String.valueOf(System.currentTimeMillis() / 1000));
			
			String strUid = tmpMap.get("uid");
			String strBindTime = tmpMap.get("bind_time");
			String strBindStatus = tmpMap.get("bind_status");
			String strLinked = strBindStatus.equals("BINDED")? "Y" : "N";
			logger.info("strUid = " + strUid);
			logger.info("strBindTime = " + strBindTime);
			logger.info("strBindStatus = " + strBindStatus);
			logger.info("strLinked = " + strLinked);
			
			// Step 3. Check UID is exist? If 'not' -> create one, if 'yes' -> update status, modify time.
			LineUser lineUser;
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			logger.info("simpleDateFormat.parse(strBindTime) = " + simpleDateFormat.parse(strBindTime));
			
			if (!lineUserService.findByUid(strUid).isPresent()) {
				lineUser = new LineUser();
				lineUser.setLineUid(strUid);
				lineUser.setStatus(LineUserStatus.NORMALLY.getValue());
			}
			else {
				lineUser = lineUserService.findByUid(strUid).get();
			}
			
			lineUser.setModifyTime(simpleDateFormat.parse(strBindTime));
			lineUser.setLinked(strLinked);

			logger.info("lineUser = " + lineUser);
			
			lineUserRepository.save(lineUser);
			
			userTrackService.add(lineUser.getId(), strBindStatus.equals("BINDED")? LineUserTrackSource.BINDED : LineUserTrackSource.UNBINDED);
			
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(e);
		}
	    
	    return ResponseEntity.ok().build();
	}
}
