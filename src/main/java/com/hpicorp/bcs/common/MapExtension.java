package com.hpicorp.bcs.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MapExtension {

	private MapExtension() {		
	}

	/**
	 * 將資料進行 key : value Map整理
	 * 資料來源的組合必須是 "tarKey" : key, "tarValue" : value 
	 * 會轉換為 key : value
	 * @param List<Map<String, Object>>
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> keyValueProcess(List<Map<String, Object>> maps) {
		Map<String, Object> result = new HashMap<>();
		if (maps == null) {
			return result;
		}
		for (Map<String, Object> map : maps) {
			Object tempKey = map.get("tarKey");
			Object tempValue = map.get("tarValue");
			if (tempKey == null || tempValue == null) {
				continue;
			}
			String key = String.valueOf(tempKey);
			result.put(key, tempValue);
		}
		return result;
	}
}
