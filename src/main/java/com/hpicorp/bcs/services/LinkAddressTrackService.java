package com.hpicorp.bcs.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.common.CsvGenerator;
import com.hpicorp.bcs.entities.LinkAddressTrack;
import com.hpicorp.bcs.entities.dto.CustomLinkAddressTrack;
import com.hpicorp.bcs.entities.dto.CustomLinkAddressTrackDetail;
import com.hpicorp.bcs.repositories.LinkAddressTrackRepository;

@Service
public class LinkAddressTrackService {

	@Autowired
	private LinkAddressTrackRepository linkAddressTrackRepository;

	public Page<LinkAddressTrack> getLinkAddressTrack(Pageable pageable) {
		return linkAddressTrackRepository.findAll(pageable);
	}

	public List<LinkAddressTrack> getLinkAddressTrack() {
		return (List<LinkAddressTrack>) linkAddressTrackRepository.findAll();
	}

	public void insert(LinkAddressTrack linkAddressTrack) {
		linkAddressTrackRepository.save(linkAddressTrack);
	}

	public void save(LinkAddressTrack linkAddressTrack) {
		linkAddressTrackRepository.save(linkAddressTrack);
	}

	public Optional<LinkAddressTrack> findById(long id) {
		return linkAddressTrackRepository.findById(id);
	}

	public void deleteById(long id) {
		linkAddressTrackRepository.deleteById(id);
	}

	public String getLinkedByUserID(long userid) {
		return linkAddressTrackRepository.getLinkedByUserID(userid);
	}

	public List<Object[]> getLinkAddressTrackByName(String name) {
		return linkAddressTrackRepository.getLinkAddressTrackByName(name);
	}
	
	/**
	 * 追蹤連結成效列表
	 * @param pageable
	 * @return
	 */
	public Page<CustomLinkAddressTrack> getLinkAddressTrackBypage(Pageable pageable) {
		Page<Object[]> results = linkAddressTrackRepository.getLinkAddressTrackByPage(pageable);
		List<CustomLinkAddressTrack> list = new ArrayList<>();
		results.getContent().stream().forEach(record -> {
			CustomLinkAddressTrack track = new CustomLinkAddressTrack(record[1].toString(), record[2].toString(), Long.valueOf(record[3].toString()), Integer.valueOf(record[4].toString()), Integer.valueOf(record[0].toString()));
		    list.add(track);
		});
		return new PageImpl<>(list, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), results.getTotalElements());
	}
	
	/**
	 * 追蹤連結成效單筆依據日期
	 * @param id 該筆 ID
	 * @return
	 */
	public Page<CustomLinkAddressTrackDetail> getLinkAddressTrackDetail(Long id, Pageable pageable) {
		Page<Object[]> results = linkAddressTrackRepository.getLinkAddressTrackDetailByPage(id, pageable);
		List<CustomLinkAddressTrackDetail> list = new ArrayList<>();
		results.getContent().stream().forEach(record -> {
			CustomLinkAddressTrackDetail detail = new CustomLinkAddressTrackDetail(record[0].toString(), 
					                                                               Integer.valueOf(record[1].toString()), 
					                                                               Integer.valueOf(record[2].toString()));
			list.add(detail);
		});
		return new PageImpl<>(list,
				results.getPageable(),
				results.getTotalElements());
	}

	/**
	 * 單筆追蹤連結點擊成效 匯出報表
	 * @param id
	 * @param response
	 */
	public void exportDetailReportByLinkAddressListId(Long id, HttpServletResponse response) {
		List<List<Object>> result = new ArrayList<>();
		String[] header = { "日期", "點擊次數", "點擊人數"};
		List<Object[]> detailList = this.linkAddressTrackRepository.getLinkAddressTrackDetailByList(id);
		for (Object[] reportBody : detailList) {
			result.add(Arrays.asList(reportBody[0].toString(),
									 reportBody[1].toString(),
									 reportBody[2].toString())
					                 );
		}
		CsvGenerator.writeWithResponse(response, header, result);
		
	}

	public void exportDetailUidReportByLinkAddressListId(Long id, HttpServletResponse response) {
		List<List<Object>> result = new ArrayList<>();
		String[] header = {"UID"};
		List<String> uidList = this.linkAddressTrackRepository.getTrackDetailUidBylinkaddresslistId(id);
		for (String uid : uidList) {
			result.add(Arrays.asList(uid));
		}
		CsvGenerator.writeWithResponse(response, header, result);
		
	}
	

}
