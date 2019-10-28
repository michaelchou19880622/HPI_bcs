package com.hpicorp.bcs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpicorp.bcs.entities.BcsLineUser;

public interface BcsLineUserRepository extends JpaRepository<BcsLineUser, Long> {

	@Query(value = "select U.mid from BcsLineUser U where U.isBinded = :isBinded")
	public List<String> findIdByBindStatus(@Param("isBinded") String isBinded);
	
}
