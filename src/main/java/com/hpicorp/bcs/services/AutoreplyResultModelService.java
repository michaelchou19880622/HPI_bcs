package com.hpicorp.bcs.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.common.CsvGenerator;
import com.hpicorp.bcs.entities.dto.AutoreplyResultBody;
import com.hpicorp.bcs.entities.dto.CustomAutoreplyDetail;
import com.hpicorp.bcs.entities.dto.CustomLinkAddressTrackDetail;
import com.hpicorp.bcs.enums.DefaultConfig;
import com.hpicorp.bcs.enums.UserClickType;

@Service
@Component
public class AutoreplyResultModelService {
	
	@Autowired
	private UserClickService userClickDao;

	@Autowired
	private AutoreplyService autoreplyDao;
	
	@Autowired
	private UserClickService userClickService;
	
//	public Map<String, Object> getAutoreplyDetailDateCountBy(Long mappingId, Date since, Date untils, Integer page) throws Exception {
//		String type = UserClickType.AUTOREPLY.toString();
//		Map<String, Object> result = new HashMap<>();
//		Map<String, Object> resultContent = new HashMap<>();
//		Integer allPage = this.userClickDao.getDateCountPageBy(mappingId, type, since, untils);
//		List<Map<String, String>> sumContentList = this.userClickDao.getDateAndCountBy(mappingId, type, since, untils, page);
//		List<Map<String, String>> countContentList = this.userClickDao.getDistinctCountCountAndDateBy(mappingId, type, since, untils, page);
//		Map<String, Integer> sumContent = this.setKeyDateAndIntegerValue(sumContentList);
//		Map<String, Integer> countContent = this.setKeyDateAndIntegerValue(countContentList);
//		String keyword = this.autoreplyDao.getKeywordById(mappingId);
//		for (Entry<String, Integer> entry : sumContent.entrySet()) {
//			Map<String, Integer> content = new HashMap<>();
//			String key = entry.getKey();
//			Integer sum = entry.getValue();
//			Integer count = countContent.get(key) == null ? 0 : countContent.get(key);
//			content.put("sum", sum);
//			content.put("count", count);
//			resultContent.put(key, content);
//		}
//		result.put("title", keyword);
//		result.put("total", allPage);
//		result.put("page", page);
//		result.put("content", new TreeMap<>(resultContent));
//		return result;
//	}
	
	public Map<String, Object> getByAutoreplyResultBody(AutoreplyResultBody body) throws Exception {
		if (body == null) {
			throw new Exception("Body can't be 'NULL'");
		}
		Page<Map<String, Object>> pageContent = null;
		Integer page = body.getPage();
 		String keyword = body.getKeyword();
 		Date period = body.getPeriod();
		Pageable pageable = PageRequest.of(page, DefaultConfig.PAGESIZE.getValue());
 		if (keyword == null) {
 			pageContent = this.userClickDao.getByPeriod(period, pageable);
 		} else {
 			pageContent = this.userClickDao.getByKeywordAndPeriod(keyword, period, pageable);
 		}
 		if (pageContent != null) {
 			return this.getReturnContent(pageContent);
 		}
		return null;
	}
	
	private Map<String, Object> getReturnContent(Page<Map<String, Object>> content) throws Exception{
		if (content == null) {
			throw new Exception("Result is not found");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultContent = new HashMap<String, Object>();
		List<Map<String, Object>> listResults = content.getContent();
		for (Map<String, Object> map : listResults) {
			try {
				Map<String, Object> keyContent = new HashMap<String, Object>();
				String key = map.get("keyword").toString();
				keyContent.put("id", map.get("id"));
				keyContent.put("user", map.get("user"));
				keyContent.put("status", map.get("status"));
				keyContent.put("period", map.get("period"));
				keyContent.put("createtime", map.get("createtime"));
				keyContent.put("count", map.get("count"));
				resultContent.put(key, keyContent);
			} catch (Exception e) {
				continue;
			}
		}
		result.put("total", content.getTotalPages());
		result.put("page", content.getNumber());
		result.put("content", resultContent);
		return result;
	}
	
	private Map<String, Integer> setKeyDateAndIntegerValue(List<Map<String, String>> content) {
		Map<String, Integer> resultContent = new HashMap<String, Integer>();
		for (Map<String, String> map : content) {
			try {
				String key = String.valueOf(map.get("date"));
				String tmpCount = String.valueOf(map.get("count"));
				Integer count = Integer.parseInt(tmpCount);
				resultContent.put(key, count);
			} catch (Exception e) {
				continue;
			}
		}
		return resultContent;
	}
	
	public List<String> getUidByMappingIdAndBetweenDate(Long mappingId, Date since, Date untils) throws Exception {
		String type = UserClickType.AUTOREPLY.toString();
		List<String> result = this.userClickDao.getUidByTypeAndMappingBetweenDate(type, mappingId, since, untils);
		return result;
	}

	public Page<CustomAutoreplyDetail> getAutoreplyDetailDateCountByPage(Long mappingId, Date since, Date untils,
			Pageable pageable) {
		Page<Object[]> results = this.userClickService.getAutoreplyByPage(mappingId, UserClickType.AUTOREPLY.toString(), since, untils, pageable);
		List<CustomAutoreplyDetail> list = new ArrayList<>();
		results.getContent().stream().forEach(record -> {
			CustomAutoreplyDetail detail = new CustomAutoreplyDetail(record[0].toString(), 
	                                                                 Integer.valueOf(record[1].toString()), 
	                                                                 Integer.valueOf(record[2].toString()));
			list.add(detail);
		});
		return new PageImpl<>(list,
				results.getPageable(),
				results.getTotalElements());
	}

	public void exportUidByAutoreplyIdAndBetweenDate(Long mappingId, Date since, Date untils,HttpServletResponse response) {
		List<List<Object>> result = new ArrayList<>();
		String[] header = {"UID"};
		List<String> uidList = this.userClickService.getUidByTypeAndMappingBetweenDate(UserClickType.AUTOREPLY.toString(), mappingId, since, untils);
		for (String uid : uidList) {
			result.add(Arrays.asList(uid));
		}
		CsvGenerator.writeWithResponse(response, header, result);
		
	}

	public void exportCsvByAutoreplyIdAndBetweenDate(Long mappingId, Date since, Date untils,
			HttpServletResponse response) {
		List<List<Object>> result = new ArrayList<>();
		String[] header = { "回覆日期", "回應次數", "回應人數"};
		List<Object[]> detailList = this.userClickService.getAutoreplyByList(mappingId, UserClickType.AUTOREPLY.toString(), since, untils);
		for (Object[] reportBody : detailList) {
			result.add(Arrays.asList(reportBody[0].toString(),
									 reportBody[1].toString(),
									 reportBody[2].toString())
					                 );
		}
		CsvGenerator.writeWithResponse(response, header, result);
	}
	
}
