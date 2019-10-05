package com.hpicorp.bcs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hpicorp.bcs.entities.LinkAddress;
import com.hpicorp.bcs.repositories.LinkAddressListRepository;
import com.hpicorp.bcs.repositories.LinkAddressRepository;

@Service
public class LinkAddressService {

	@Autowired
	private LinkAddressRepository linkAddressRepository;
	
	@Autowired
	private LinkAddressListRepository linkAddressListRepository;	
	
	
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
	
	public Optional<LinkAddress> findById(long id) {
		return linkAddressRepository.findById(id);
	}
	
	public void deleteById(long id) {
		linkAddressRepository.deleteById(id);
	}	
	
	public void deleteListByLinkID(long id) {
		linkAddressListRepository.deleteByLinkId(id);
	}
	
}
