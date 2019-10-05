package com.hpicorp.bcs.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.common.DateTimeModel;
import com.hpicorp.bcs.services.AutoreplyResultModelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/result/reply/autoreply/detail")
public class AutoreplyResultDetailController {

	@Autowired
	private AutoreplyResultModelService model;

	/**
	 * 供 依照關鍵字編號取得日期範圍內的使用統計(count)資料 包含 使用人數 和 人次 API
	 * @param mappingId 關鍵字編號
	 * @param since 開始日期
	 * @param untils 結束日期
	 * @param page 分頁
	 * @return
	 */
	@RequestMapping(path = "/date", method = RequestMethod.GET)
	public ResponseEntity<?> getDateDetailCountByAutoReplyId(
			@RequestParam(value = "id", required = false) Long mappingId,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date since,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date untils,
			@RequestParam(value = "page", required = false) Integer page) {
		if (mappingId == null) {
			return ResponseEntity.badRequest().body("id can't be null");
		}
		page = (page == null) ? 0 : page;
		try {
			since = since == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(since);
			untils = untils == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(untils);
			Map<String, Object> result = this.model.getAutoreplyDetailDateCountBy(mappingId, since, untils, page);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * 供 提供關鍵字編號及時間區間查找使用過的User UUID API
	 * @param mappingId 關鍵字編號
	 * @param since 開始日期
	 * @param untils 結束日期
	 * @return
	 */
	@RequestMapping(path = "/uid", method = RequestMethod.GET)
	public ResponseEntity<Object> getUidsByAutoReplyId(
			@RequestParam(value = "id", required = false) Long mappingId,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date since,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date untils) {
		if (mappingId == null) {
			return ResponseEntity.badRequest().body("id can't be null");
		}
		try {
			since = since == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(since);
			untils = untils == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(untils);
			List<String> uids = model.getUidByMappingIdAndBetweenDate(mappingId, since, untils);
			return ResponseEntity.ok(uids);
		} catch (Exception e) {
			log.error("getAutoreplyDateDetailCountByAutoReplyId", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
