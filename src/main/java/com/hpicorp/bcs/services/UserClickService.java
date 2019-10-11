package com.hpicorp.bcs.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.UserClick;
import com.hpicorp.bcs.enums.DefaultConfig;
import com.hpicorp.bcs.enums.UserClickType;
import com.hpicorp.bcs.repositories.UserClickRepository;

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
	
	public void save(UserClick entity) throws Exception {
		this.userClickRepository.save(entity);
	}
	
	public Integer getDateCountPageBy(Long mappingId, String type, Date since, Date untils) throws Exception {
		List<Long> counts = this.userClickRepository.getRowCountByMappingIdAndTypeAndBetweenDate(mappingId, type, since, untils);
		if (counts == null || counts.size() <= 0) {
			return 0;
		}
		Integer sum = counts.size();
		Double result = (double)sum/(double)DefaultConfig.PAGESIZE.getValue();
		Double temp = Math.ceil(result);
		return temp.intValue();
	}
	
	public List<Map<String, String>> getDateAndCountBy(Long mappingId, String type, Date since, Date untils, Integer page) throws Exception {
		Integer pageSize = DefaultConfig.PAGESIZE.getValue();	
		Integer pageIndex = page*pageSize;
		return this.userClickRepository.getDateAndCountByMappingIdAndType(mappingId, type, since, untils, pageIndex, pageSize);
	}
	
	public List<Map<String, String>> getDistinctCountCountAndDateBy(Long mappingId, String type, Date since, Date untils, Integer page) throws Exception {
		Integer pageSize = DefaultConfig.PAGESIZE.getValue();	
		Integer pageIndex = page*pageSize;
		return this.userClickRepository.getDistinctCountAndDateByMappingIdAndType(mappingId, type, since, untils, pageIndex, pageSize);
	}
	
	public Page<Object[]> getAutoreplyByPage(Long mappingId, String type, Date since, Date untils, Pageable pageable) {
		return this.userClickRepository.getAutoreplyDetailByPage(mappingId, type, since, untils, pageable);
	}
	
	public List<Object[]> getAutoreplyByList(Long mappingId, String type, Date since, Date untils) {
		return this.userClickRepository.getAutoreplyDetailByList(mappingId, type, since, untils);
	}
}
