package com.hpicorp.bcs.controllers;

import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.common.DateTimeModel;
import com.hpicorp.bcs.entities.dto.CustomAutoreplyDetail;
import com.hpicorp.bcs.services.AutoreplyResultModelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/result/reply/autoreply/detail")
public class AutoreplyResultDetailController {

	@Autowired
	private AutoreplyResultModelService autoreplyResultModelService;

	/**
	 * 供 依照關鍵字編號取得日期範圍內的使用統計(count)資料 包含 使用人數 和 人次 API
	 * @param mappingId 關鍵字編號
	 * @param since 開始日期
	 * @param untils 結束日期
	 * @param page 分頁
	 * @return
	 */
	@GetMapping("/date")
	public ResponseEntity<Object> getDateDetailCountByAutoReplyId(
			@RequestParam(value = "id", required = false) Long mappingId,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date since,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date untils,
			@PageableDefault(size = 10) Pageable pageable) {
		if (mappingId == null) {
			return ResponseEntity.badRequest().body("id can't be null");
		}
		try {
			since = since == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(since);
			untils = untils == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(untils);
			Page<CustomAutoreplyDetail> result = this.autoreplyResultModelService.getAutoreplyDetailDateCountByPage(mappingId, since, untils, pageable);
			return ResponseEntity.ok().body(result);
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
	@GetMapping("/uid")
	public ResponseEntity<Object> getUidsByAutoReplyId(
			@RequestParam(value = "id", required = false) Long mappingId,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date since,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date untils,
			HttpServletResponse response) {
		if (mappingId == null) {
			return ResponseEntity.badRequest().body("id can't be null");
		}
		try {
			since = since == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(since);
			untils = untils == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(untils);
			this.autoreplyResultModelService.exportUidByAutoreplyIdAndBetweenDate(mappingId, since, untils, response);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("匯出關鍵字UID報表錯誤={}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 供 提供關鍵字編號及時間區間 匯出 觸發成效
	 * @param mappingId 關鍵字編號
	 * @param since 開始日期
	 * @param untils 結束日期
	 * @return
	 */
	@GetMapping("/csv")
	public ResponseEntity<Object> getCsvByAutoReplyId(
			@RequestParam(value = "id", required = false) Long mappingId,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date since,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date untils,
			HttpServletResponse response) {
		if (mappingId == null) {
			return ResponseEntity.badRequest().body("id can't be null");
		}
		try {
			since = since == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(since);
			untils = untils == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(untils);
			this.autoreplyResultModelService.exportCsvByAutoreplyIdAndBetweenDate(mappingId, since, untils, response);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("匯出關鍵字觸發成效報表錯誤={}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	

}
