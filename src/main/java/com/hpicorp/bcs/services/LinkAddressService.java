package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hpicorp.bcs.entities.LinkAddress;
import com.hpicorp.bcs.repositories.LinkAddressRepository;

@Service
public class LinkAddressService {

	@Autowired
	private LinkAddressRepository linkAddressRepository;
	
	public Page<LinkAddress> getAllLinkAddress(Pageable pageable) {
		return linkAddressRepository.findAll(pageable);
	}
	
	public List<LinkAddress> getAllLinkAddress() {
		return (List<LinkAddress>) linkAddressRepository.findAll();
	}
	
	public void insert(LinkAddress linkAddress) {
		linkAddressRepository.save(linkAddress);
	}
	
	public void save(LinkAddress linkAddress) {
		linkAddressRepository.save(linkAddress);		
	}
	
	public Optional<LinkAddress> findById(Long id) {
		return linkAddressRepository.findById(id);
	}
	
	public void deleteById(Long id) {
		linkAddressRepository.deleteById(id);
	}	
	
	
}
