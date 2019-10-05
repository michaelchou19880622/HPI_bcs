package com.hpicorp.bcs.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.common.DateTimeModel;
import com.hpicorp.bcs.services.UserResultLogModelService;

@RestController
@RequestMapping(path="/result/log")
public class UserResultLogController {

	@Autowired
	private UserResultLogModelService userResultLogModel;
	
	/**
	 * 提供使用者狀態和時間區間查找人數和每日人數 API
	 * @param state @see LineUserTrackSource
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(path="/date",method=RequestMethod.GET)
	public ResponseEntity<Object> getByDate(@RequestParam(value="state", required=false) String state, @RequestParam("from") @DateTimeFormat(pattern="yyyy/MM/dd") Date from, @RequestParam("to") @DateTimeFormat(pattern="yyyy/MM/dd") Date to) {
		try {
			if (from == null || to== null) {
				return ResponseEntity.badRequest().body("UnAccepted Get Param");
			}
			Map<String, Object> result = this.userResultLogModel.getLogBetweenDate(from, to, state);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 查找該時間區間內的各狀態的總人數 API
	 * @param from 開始日期
	 * @param to 結束日期
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Object> get(@RequestParam("from") @DateTimeFormat(pattern="yyyy/MM/dd") Date from, @RequestParam("to") @DateTimeFormat(pattern="yyyy/MM/dd") Date to) {
		try {
			if (from == null || to== null) {
				return ResponseEntity.badRequest().body("UnAccepted Get Param");
			}
			from = from == null ? DateTimeModel.initialDate() : DateTimeModel.minimizeTime(from);
			to = to == null ? DateTimeModel.maximumTime(new Date()) : DateTimeModel.maximumTime(to);
			Map<String, Integer> result = this.userResultLogModel.getUserResultLogCountListBetweenDate(from, to);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
