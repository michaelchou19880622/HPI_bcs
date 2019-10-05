package com.hpicorp.bcs.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.enums.LineUserTrackSource;

@Service
public class UserResultLogModelService {

	@Autowired
	private UserTrackService userTrackDao;
	
	public Map<String, Integer> getUserResultLogCountListBetweenDate(Date from, Date to) throws Exception {
		Map<String, Integer> result = new HashMap<String, Integer>();
		 for (LineUserTrackSource b : LineUserTrackSource.values()) {
			 String key = b.toString();
			 Integer count = this.userTrackDao.getCountBySourceAndDate(b, from, to);
			 result.put(key, count);
		 }
		return result;
	}
	
	public Map<String, Object> getLogBetweenDate(Date from, Date to, String source) throws Exception {
		Map<String, Object> content = new HashMap<String, Object>();
		List<LineUserTrackSource> sources = LineUserTrackSource.getListByString(source);

		for (LineUserTrackSource s : sources) {
			Long count = 0L;
			Map<String, Object> objectContent = new HashMap<String, Object>();
			Map<String, Object> objects = new HashMap<String, Object>();
			Long startDB = System.currentTimeMillis();
			List<Map<String, Object>> datas =  this.userTrackDao.getByDate(from, to, s.toString());
			Long endDB = System.currentTimeMillis();
			System.err.println("DB 查詢時間 => " + (endDB - startDB) / 1000 + " 秒");
			for (Map<String, Object> map : datas) {
				Long c = Long.valueOf(map.get("count").toString());
				count+=c ;
				objects.put(map.get("date").toString(), c);
			}
			objectContent.put("count", count);
			objectContent.put("content", objects);
			content.put(s.toString(), objectContent);
		}
		return content;
	}
	
}
