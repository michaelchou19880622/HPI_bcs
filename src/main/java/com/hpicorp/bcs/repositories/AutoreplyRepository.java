package com.hpicorp.bcs.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.Autoreply;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface AutoreplyRepository extends PagingAndSortingRepository<Autoreply, Long> {

	@Transactional
	@Query(value = "select A from Autoreply A where A.type <> 'DEFAULT' and A.period = 'DAY' and A.datetimeBegin > :today and A.datetimeEnd > :today")
	public Page<Autoreply> getAutoreplyByInActivePeriod(Pageable pageable, @Param("today") Date today);

	@Transactional
	@Query(value = "select A from Autoreply A where A.type <> 'DEFAULT' and A.period = 'DAY' and A.datetimeBegin < :today and A.datetimeEnd < :today")
	public Page<Autoreply> getAutoreplyByExpiredPeriod(Pageable pageable, @Param("today") Date today);

	@Transactional
	@Query(value = "select A from Autoreply A where A.type <> 'DEFAULT' and ( A.period = 'FOREVER' ) or (A.period = 'DAY' and A.datetimeBegin <= :today and A.datetimeEnd >= :today )")
	public Page<Autoreply> getAutoreplyByActivePeriod(Pageable pageable, @Param("today") Date today);

	@Transactional
	@Query(value = "select A from Autoreply A where A.type <> 'DEFAULT' ")
	public Page<Autoreply> getAutoreplyListWithoutDefault(Pageable pageable);

	public List<Autoreply> findByModifyUser(String modifyUser);

}
