package com.hpicorp.bcs.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.hpicorp.bcs.entities.LineUser;
import com.hpicorp.bcs.entities.LinkAddress;
import com.hpicorp.bcs.entities.LinkAddressList;
import com.hpicorp.bcs.entities.LinkAddressTrack;
import com.hpicorp.bcs.services.LineUserService;
import com.hpicorp.bcs.services.LinkAddressService;
import com.hpicorp.bcs.services.LinkAddressTrackService;

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
	
	@GetMapping(value = "/linkaddress/all")
	public @ResponseBody Page<LinkAddress> getAllLinkAddress(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable){
		return linkAddressService.getAllLinkAddress(pageable);
	}
	
	@GetMapping(value = "/linkaddress/allData")
	public @ResponseBody List<LinkAddress> getAllLinkAddressList(){
		return linkAddressService.getAllLinkAddress();
	}	
	
	@DeleteMapping("/linkaddress/{id}")
	public void deleteLinkAddress(@PathVariable long id) {
		linkAddressService.deleteById(id);
	}
	
	@GetMapping(value = "/linkaddress/link/{uid}/{id}/{isMobile}")
	public ResponseEntity<String> getLinkAddress(@PathVariable String uid, @PathVariable Long id, @PathVariable Boolean isMobile) {
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
    
	/*
	 * The Method for LIFF calling 
	 * userid: 33 codes of LINE UID
	 * id : LinkAddress ID
	 */
	@GetMapping(value = "/linkaddress/link/{userid}/{id}")
	public RedirectView getLinkAddressOld(Device device, @PathVariable String userid, @PathVariable long id){		
		Optional<LinkAddress> linkAddressOptional = linkAddressService.findById(id);
				
		if (! linkAddressOptional.isPresent())
			return new RedirectView();
					
		LinkAddressTrack linkAddressTrack = new LinkAddressTrack();
		linkAddressTrack.setLinkAddressId(id);
		linkAddressTrack.setCreateTime(new Date());
		Long lineuserId = lineUserService.getIDByLINEUID(userid);
		linkAddressTrack.setLineUserId(lineuserId);
		Optional<LineUser> usr = lineUserService.findById(lineuserId);
		String linked = "UNBINDED";
		if ( usr.isPresent() && usr.get().getLinked() != null) {
			linked = usr.get().getLinked().trim().equals("Y") ? "BINDED" : "UNBINDED";
		}

		boolean isMobile = checkIsMobile(device);
		if (!isMobile) linked = "NOTPHONE";
		
		long listid = 0;
		String projectUrl = "";

		final String condition = linked;
		List<LinkAddressList> linkAddressList = linkAddressOptional.get().getLinkAddressList().stream()
			    .filter(p -> p.getType().equals(condition)).collect(Collectors.toList());
		if (! linkAddressList.isEmpty()) {
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
	
	@GetMapping(value = "/linkaddress/report/getuids/{id}")
	public @ResponseBody List<Map<String, String>> getTrackDetailBylinkaddresslistId(@PathVariable long id){	
		List<String> uidList = linkAddressTrackService.getTrackDetailBylinkaddresslist_id(id);
		List<Map<String, String>> result = new ArrayList<>();
		for(String uid : uidList) {
			Map<String, String> mapped = new HashMap<>();
			mapped.put("UID", uid);
			result.add(mapped);
		}
		return result;
	}	
	
	@RequestMapping("/linkaddress/{id}")
	public RedirectView redirectToLIFF(@PathVariable long id) {
		String projectUrl = "line://app/1562106995-1rNpOLKq?linkaddress=" + id;
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(projectUrl);
		return redirectView;
	}
	
	@PostMapping(path = "/linkaddress/new") 
	public @ResponseBody Map<String, String> createLinkAddress(@RequestBody LinkAddress linkAddress ) {

		for(LinkAddressList d : linkAddress.getLinkAddressList()) {
			d.setLinkAddress(linkAddress);
		}
		linkAddress.setModifyTime(new Date());
		linkAddressService.insert(linkAddress);
		Map<String, String> mapped = new HashMap<>();
		mapped.put(STATUS, "Success");
		mapped.put("id", linkAddress.getId().toString());
		return mapped;
	}
		
	@PutMapping("/linkaddress/{id}")
	public @ResponseBody Map<String, String> updateLinkAddress(@RequestBody LinkAddress linkAddress, @PathVariable long id) {

		Optional<LinkAddress> linkAddressOptional = linkAddressService.findById(id);

		Map<String, String> mapped = new HashMap<>();
		
		if (!linkAddressOptional.isPresent()) {
			mapped.put(STATUS, "Failure");
			return mapped;
		}

		linkAddress.setId(id);
		for(LinkAddressList d : linkAddress.getLinkAddressList()) {			
			d.setLinkAddress(linkAddress);
		}
		linkAddress.setModifyTime(new Date());
		linkAddressService.save(linkAddress);

		mapped.put(STATUS, "Success");
		mapped.put("id", linkAddress.getId().toString());
		return mapped;
	}	
	
}
