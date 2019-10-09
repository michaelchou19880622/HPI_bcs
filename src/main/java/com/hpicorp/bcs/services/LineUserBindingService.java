package com.hpicorp.bcs.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpicorp.bcs.config.ApProperties;
import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.entities.dto.ApiResponse;
import com.hpicorp.bcs.enums.LineUserBindStatus;
import com.hpicorp.bcs.enums.LineUserStatus;
import com.hpicorp.bcs.enums.LineUserTrackSource;
import com.hpicorp.bcs.repositories.LineUserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LineUserBindingService {

	@Autowired
	private LineUserRepository lineUserRepository;
	
	@Autowired
	private LineUserService lineUserService;
	
	@Autowired
	private UserTrackService userTrackService;

	@Autowired
	private ApProperties properties;
	
	public ResponseEntity<Object> bindLineUserAndSetUserTrackStatus(String ChannelId, String updateModel) {
		log.info("ApProperties lineChannelId = {}", properties.getLineChannelId());
		log.info("@PathVariable ChannelId = {}", ChannelId);
		log.info("@RequestBody RequestBody = {}", updateModel);
		
		try {
			// Step 1. Check if ChanelID is correct? If 'not' -> return error code, message, if 'yes' -> Step 2.
			if (!ChannelId.equals(properties.getLineChannelId())) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Line channel id not match."));
			}
			
			// Step 2. Parse request body to json string and map to UserBindModel
			Map<String, String> mapBodyContent = new HashMap<String, String>();
			ObjectMapper objMapper = new ObjectMapper();

			mapBodyContent = objMapper.readValue(updateModel, new TypeReference<HashMap<String, String>>() {});
			mapBodyContent.put("status", mapBodyContent.get("bind_status"));
			mapBodyContent.put("time", String.valueOf(System.currentTimeMillis() / 1000));
			
			String strUid = mapBodyContent.get("uid");
			String strBindTime = mapBodyContent.get("bind_time");
			String strBindStatus = mapBodyContent.get("bind_status");
			String strLinked = null;
			
			LineUserTrackSource lineUserTrackStatus = null;
			
			if (LineUserBindStatus.STATUS_BINDED.toString().equals(strBindStatus)) {
				strLinked = LineUserBindStatus.BINDED.getValues();
				lineUserTrackStatus = LineUserTrackSource.BINDED;
			} else if (LineUserBindStatus.STATUS_UNBIND.toString().equals(strBindStatus)) {
				strLinked = LineUserBindStatus.UNBINDED.getValues();
				lineUserTrackStatus = LineUserTrackSource.UNBINDED;
			} else {
				log.info("strBindStatus = {}, strLinked = {}, ", strBindStatus, updateModel);
				return ResponseEntity.badRequest().body("Unknown status : " + strBindStatus);
			}
			
			// Step 3. Check UID is exist? If 'not' -> create one, if 'yes' -> update status, modify time.
			LineUser lineUser;
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
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

			log.info("lineUser = {}", lineUser);
			
			lineUserRepository.save(lineUser);
			
			log.info("lineUserTrackStatus = {}", lineUserTrackStatus);
			
			userTrackService.add(lineUser.getId(), lineUserTrackStatus);
			
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(e);
		}

	    return ResponseEntity.ok(updateModel);
	}
}
