package com.hpicorp.bcs.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpicorp.core.enums.UserClickType;
import com.hpicorp.core.repository.UserClickRepository;

@Component
@Repository
public class UserClickService {

	@Autowired
	private UserClickRepository userClickRepository;
	
	public List<String> getUidByTypeAndMappingBetweenDate(String type, Long mappingId, Date since, Date untils) {
		return this.userClickRepository.getUidByTypeAndMappingIdAndCreateTimeBetweenSinceAndUntils(type, mappingId, since, untils);
	}
	
	public Page<Map<String, Object>> getByPeriod(Date period, Pageable pageable) {
		return this.userClickRepository.getByPeriod(period, UserClickType.AUTOREPLY.toString(), pageable);
	}
	public Page<Map<String, Object>> getByKeywordAndPeriod(String keyword, Date period, Pageable pageable) {
		return this.userClickRepository.getByKeywordAndPeriod(keyword, period, UserClickType.AUTOREPLY.toString(), pageable);
	}
	
	public Page<Object[]> getAutoreplyByPage(Long mappingId, String type, Date since, Date untils, Pageable pageable) {
		return this.userClickRepository.getAutoreplyDetailByPage(mappingId, type, since, untils, pageable);
	}
	
	public List<Object[]> getAutoreplyByList(Long mappingId, String type, Date since, Date untils) {
		return this.userClickRepository.getAutoreplyDetailByList(mappingId, type, since, untils);
	}
}
