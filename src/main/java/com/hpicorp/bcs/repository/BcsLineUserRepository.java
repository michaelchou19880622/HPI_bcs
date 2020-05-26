package com.hpicorp.bcs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.BcsLineUser;

public interface BcsLineUserRepository extends JpaRepository<BcsLineUser, Long> {

	@Transactional(readOnly = true, timeout = 300)
	@Query(value = "select U.mid from BcsLineUser U where U.isBinded = :isBinded")
	public List<String> findIdByBindStatus(@Param("isBinded") String isBinded);

	@Transactional(readOnly = true, timeout = 600)
	@Query(value = "select U.mid from BcsLineUser U where U.status = :status")
	public List<String> findIdByStatus(@Param("status") String status);
	
	@Transactional(readOnly = true, timeout = 600)
	@Query(value = "select U.mid from BcsLineUser U where U.status in ('BINDED', 'UNBIND')")
	public List<String> findIdByStatusAll();
	
}
