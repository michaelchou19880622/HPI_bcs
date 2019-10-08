package com.hpicorp.bcs.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.LineUserTrack;
import com.hpicorp.bcs.enums.LineUserBindStatus;
import com.hpicorp.bcs.enums.LineUserTrackSource;
import com.hpicorp.bcs.repositories.LineUserTrackRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Repository
public class UserTrackService {
	
	@Autowired
	private LineUserTrackRepository lineUserTrackRepository;
	
	public List<Map<String, Object>> getByDate(Date from, Date to, String source) {
		List<String> sources = Arrays.asList(source);
		return this.getByDate(from, to, sources);
	}
	public List<Map<String, Object>> getByDate(Date from, Date to, List<String> sources) {
		return this.lineUserTrackRepository.countBySourceAndCreateTimeBetween(from, to, sources);
	}
	public Integer getCountBySourceAndDate(LineUserTrackSource source, Date from, Date to) {
		return this.lineUserTrackRepository.countBySourceAndCreateTimeBetween(source.toString(), from, to);
	}
	
	public void add(Long userId, LineUserBindStatus status) {
		try {
			LineUserTrackSource source = LineUserTrackSource.fromString(status.toString());
			this.add(userId, source);
		} catch (Exception e) {
			log.error("track Error", e);
		}
	}
	
	public void add(Long userId, LineUserTrackSource source) {
		LineUserTrack entity = new LineUserTrack();
		entity.setUserId(userId);
		entity.setSource(source.toString());
		entity.setCreateTime(new Date());
		this.lineUserTrackRepository.save(entity);
	}
}
