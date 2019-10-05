package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.LinkAddressTrack;
import com.hpicorp.bcs.repositories.LinkAddressTrackRepository;

@Service
public class LinkAddressTrackService {

	@Autowired
	private LinkAddressTrackRepository linkAddressTrackRepository;
	
	public Page<LinkAddressTrack> getLinkAddressTrack(Pageable pageable) {
		return  linkAddressTrackRepository.findAll(pageable);
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
	
	public List<Object[]>  getAllLinkAddressTrack() {
		return linkAddressTrackRepository.getLinkAddressTrack();
	}	
	
	public Page<Object[]>  getLinkAddressTrackBypage(Pageable pageable) {
		return linkAddressTrackRepository.getLinkAddressTrackBypage(pageable);
	}	
	
	public List<Object[]>  getLinkAddressTrackByName(String name) {
		return linkAddressTrackRepository.getLinkAddressTrackByName(name);
	}	
	
	public List<Object[]>  getLinkAddressTrackDetail(long id) {
		return linkAddressTrackRepository.getLinkAddressTrackDetail(id);
	}		
	
	public List<String>  getTrackDetailBylinkaddresslist_id(long id) {
		return linkAddressTrackRepository.getTrackDetailBylinkaddresslist_id(id);
	}	
	
}
