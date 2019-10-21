package com.hpicorp.bcs.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.hpicorp.bcs.services.LineUserService;
import com.hpicorp.bcs.services.LinkAddressService;
import com.hpicorp.bcs.services.LinkAddressTrackService;
import com.hpicorp.core.dto.CustomLinkAddressTrack;
import com.hpicorp.core.dto.CustomLinkAddressTrackDetail;
import com.hpicorp.core.entities.LineUser;
import com.hpicorp.core.entities.LinkAddress;
import com.hpicorp.core.entities.LinkAddressList;
import com.hpicorp.core.entities.LinkAddressTrack;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class LinkAddressController {

	@Autowired
	private LinkAddressService linkAddressService;

	@Autowired
	private LinkAddressTrackService linkAddressTrackService;

	@Autowired
	private LineUserService lineUserService;

	private static final String STATUS = "status";

	/**
	 * [Read List]追蹤連結列表
	 * @param pageable
	 * @return
	 */
	@GetMapping(value = "/linkaddress/all")
	public Page<LinkAddress> getAllLinkAddress(
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
		return linkAddressService.getAllLinkAddress(pageable);
	}

	/**
	 * [Read]追蹤連結所有清單
	 * @return
	 */
	@GetMapping(value = "/linkaddress/allData")
	public @ResponseBody List<LinkAddress> getAllLinkAddressList() {
		return linkAddressService.getAllLinkAddress();
	}

	/**
	 * [Delete]刪除追蹤連結
	 * @param id
	 */
	@DeleteMapping("/linkaddress/{id}")
	public void deleteLinkAddress(@PathVariable long id) {
		linkAddressService.deleteById(id);
	}

	/**
	 *	點開追蹤連結，LineLogin後將UID、追蹤連結id帶回查詢最終導向網址(新API)
	 * @param uid
	 * @param id
	 * @param isMobile
	 * @return
	 */
	@GetMapping(value = "/linkaddress/link/{uid}/{id}/{isMobile}")
	public ResponseEntity<String> getLinkAddress(@PathVariable String uid, @PathVariable Long id,
			@PathVariable Boolean isMobile) {
		log.info("『 LinkAddressController 』getLinkAddress(), uid => {}, id => {}, isMobile => {}", uid, id, isMobile);

		// 先查詢該 LinkAddress
		Optional<LinkAddress> linkAddressOptional = linkAddressService.findById(id);
		if (!linkAddressOptional.isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		// 查詢該用戶資訊
		Optional<LineUser> lineUser = lineUserService.findByUid(uid);
		if (!lineUser.isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		// 設置 追蹤連結紀錄
		LinkAddressTrack linkAddressTrack = new LinkAddressTrack();
		linkAddressTrack.setLinkAddressId(id);
		linkAddressTrack.setCreateTime(new Date());
		linkAddressTrack.setLineUserId(lineUser.get().getId());

		// 判斷該取得哪種連結
		String linked = "UNBINDED";
		if (null != lineUser.get().getLinked() && "Y".equals(lineUser.get().getLinked())) {
			linked = "BINDED";
		} else if (!isMobile) {
			linked = "NOTPHONE";
		}

		// 收尋連結
		String projectUrl = "";
		final String condition = linked;
		List<LinkAddressList> linkAddressList = linkAddressOptional.get().getLinkAddressList().stream()
				.filter(p -> p.getType().equals(condition)).collect(Collectors.toList());
		if (!linkAddressList.isEmpty()) {
			linkAddressTrack.setLinkAddressListId(linkAddressList.get(0).getId());
			linkAddressTrack.setCreateTime(new Date());
			projectUrl = linkAddressList.get(0).getUrl();
		}

		// 保存追蹤
		linkAddressTrackService.insert(linkAddressTrack);

		return ResponseEntity.ok().body(projectUrl);

	}

	/**
	 *	點開追蹤連結，LineLogin後將UID、追蹤連結id帶回查詢最終導向網址(舊API，為了merge先留著，之後要刪掉)
	 * @param uid
	 * @param id
	 * @param isMobile
	 * @return
	 */
	@GetMapping(value = "/linkaddress/link/{userid}/{id}")
	public RedirectView getLinkAddressOld(Device device, @PathVariable String userid, @PathVariable long id) {
		Optional<LinkAddress> linkAddressOptional = linkAddressService.findById(id);

		if (!linkAddressOptional.isPresent())
			return new RedirectView();

		LinkAddressTrack linkAddressTrack = new LinkAddressTrack();
		linkAddressTrack.setLinkAddressId(id);
		linkAddressTrack.setCreateTime(new Date());
		Long lineuserId = lineUserService.getIDByLINEUID(userid);
		linkAddressTrack.setLineUserId(lineuserId);
		Optional<LineUser> usr = lineUserService.findById(lineuserId);
		String linked = "UNBINDED";
		if (usr.isPresent() && usr.get().getLinked() != null) {
			linked = usr.get().getLinked().trim().equals("Y") ? "BINDED" : "UNBINDED";
		}

		boolean isMobile = checkIsMobile(device);
		if (!isMobile)
			linked = "NOTPHONE";

		long listid = 0;
		String projectUrl = "";

		final String condition = linked;
		List<LinkAddressList> linkAddressList = linkAddressOptional.get().getLinkAddressList().stream()
				.filter(p -> p.getType().equals(condition)).collect(Collectors.toList());
		if (!linkAddressList.isEmpty()) {
			listid = linkAddressList.get(0).getId();
			projectUrl = linkAddressList.get(0).getUrl();
		}

		linkAddressTrack.setLinkAddressListId(listid);
		linkAddressTrack.setCreateTime(new Date());
		linkAddressTrackService.insert(linkAddressTrack);

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(projectUrl);
		return redirectView;
	}

	private boolean checkIsMobile(Device device) {
		return device.isMobile();
	}
	

	/**
	 * [Create]建立追蹤連結
	 * @param linkAddress
	 * @return
	 */
	@PostMapping(path = "/linkaddress/new")
	public @ResponseBody Map<String, String> createLinkAddress(@RequestBody LinkAddress linkAddress) {

		for (LinkAddressList d : linkAddress.getLinkAddressList()) {
			d.setLinkAddress(linkAddress);
		}
		linkAddress.setModifyTime(new Date());
		linkAddressService.insert(linkAddress);
		Map<String, String> mapped = new HashMap<>();
		mapped.put(STATUS, "Success");
		mapped.put("id", linkAddress.getId().toString());
		return mapped;
	}

	/**
	 * [Update]編輯追蹤連結
	 * @param linkAddress
	 * @param id
	 * @return
	 */
	@PutMapping("/linkaddress/{id}")
	public @ResponseBody Map<String, String> updateLinkAddress(@RequestBody LinkAddress linkAddress,
			@PathVariable long id) {

		Optional<LinkAddress> linkAddressOptional = linkAddressService.findById(id);

		Map<String, String> mapped = new HashMap<>();

		if (!linkAddressOptional.isPresent()) {
			mapped.put(STATUS, "Failure");
			return mapped;
		}

		linkAddress.setId(id);
		for (LinkAddressList d : linkAddress.getLinkAddressList()) {
			d.setLinkAddress(linkAddress);
		}
		linkAddress.setModifyTime(new Date());
		linkAddressService.save(linkAddress);

		mapped.put(STATUS, "Success");
		mapped.put("id", linkAddress.getId().toString());
		return mapped;
	}

	/**
	 * 	追蹤連結成效列表 API
	 * @param pageable 分頁
	 * @return
	 */
	@GetMapping(value = "/linkaddress/reportpage")
	public Page<CustomLinkAddressTrack> getLinkAddressTrack1(@PageableDefault(size = 10) Pageable pageable) {
		return linkAddressTrackService.getLinkAddressTrackBypage(pageable);
	}

	/**
	 * 	追蹤連結成效單筆依據日期 API
	 * @param id 該筆數據 ID
	 * @param pageable 分頁
	 * @return
	 */
	@GetMapping(value = "/linkaddress/report/{id}")
	public Page<CustomLinkAddressTrackDetail> getLinkAddressTrackDetail(@PathVariable(value="id") Long id,
			@PageableDefault(size = 10) Pageable pageable) {
		return linkAddressTrackService.getLinkAddressTrackDetail(id, pageable);
	}
	
	/**
	 * 	追蹤連結成效用名稱搜尋 API
	 * @param name 搜尋名稱
	 * @param pageable 分頁
	 * @return
	 */
	@GetMapping(value = "/linkaddress/report/name/{name}")
	public @ResponseBody Page<CustomLinkAddressTrack> getLinkAddressTrackByName(@PathVariable(value="name") String name,
			@PageableDefault(value = 10) Pageable pageable) {	
		List<Object[]> results = linkAddressTrackService.getLinkAddressTrackByName(name);
		List<CustomLinkAddressTrack> list = new ArrayList<>();
		results.stream().forEach(record -> {			
			CustomLinkAddressTrack track = new CustomLinkAddressTrack(record[0].toString(),record[1].toString(),Long.valueOf(record[2].toString()),Integer.valueOf(record[3].toString()),Integer.valueOf(record[4].toString()));
		    list.add(track);
		});
		return new PageImpl<>(list,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
				results.size());
	}
	
	/**
	 * 	匯出該筆追蹤連結的所有點擊uid
	 * @param id
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/linkaddress/report/getuids/{id}")
	public ResponseEntity<Object> exportDetailUidReportByLinkAddressListId(@PathVariable(value="id") Long id,
			HttpServletResponse response) {
		this.linkAddressTrackService.exportDetailUidReportByLinkAddressListId(id, response);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 	匯出該筆追蹤連結所有點擊成效(不分頁)
	 * @param id
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/linkaddress/report/csv/{id}")
	public ResponseEntity<Object> exportDetailReportByLinkAddressListId(@PathVariable(value="id") Long id,
			HttpServletResponse response) {
		this.linkAddressTrackService.exportDetailReportByLinkAddressListId(id, response);
		return ResponseEntity.ok().build();
	}

}
