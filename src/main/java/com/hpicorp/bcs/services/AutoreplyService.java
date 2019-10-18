package com.hpicorp.bcs.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hpicorp.bcs.entities.Autoreply;
import com.hpicorp.bcs.repositories.AutoreplyDetailRepository;
import com.hpicorp.bcs.repositories.AutoreplyRepository;

@Service
public class AutoreplyService {

	@Autowired
	private AutoreplyRepository autoreplyRepository;
	
	@Autowired
	private AutoreplyDetailRepository autoreplyDetailRepository;	
	
	public Page<Autoreply> getAutoreplyListWithoutDefault(Pageable pageable) {
		return autoreplyRepository.getAutoreplyListWithoutDefault(pageable);
	}
	
	public void insert(Autoreply autoreply) {
		autoreplyRepository.save(autoreply);
	}
	
	public void save(Autoreply autoreply) {
		autoreplyRepository.save(autoreply);		
	}
	
	public Optional<Autoreply> findById(Long id) {
		return autoreplyRepository.findById(id);
	}
	
	public void deleteById(Long id) {
		autoreplyRepository.deleteById(id);
	}	
	
	public void deleteAutoreplyDetailByAutoreplyID(Long id) {
		autoreplyDetailRepository.deleteByAutoreplyID(id);
	}
	
	public Page<Autoreply> getAutoreplyByInActivePeriod(Pageable pageable, Date today) {
		return autoreplyRepository.getAutoreplyByInActivePeriod(pageable,today );
	}	
	
	public Page<Autoreply> getAutoreplyByExpiredPeriod(Pageable pageable, Date today) {
		return autoreplyRepository.getAutoreplyByExpiredPeriod(pageable,today );
	}
	
	public Page<Autoreply> getAutoreplyByActivePeriod(Pageable pageable, Date today) {
		return autoreplyRepository.getAutoreplyByActivePeriod(pageable,today );
	}
	
}
