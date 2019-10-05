package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.SystemUserRoleFunction;;

public interface SystemUserRoleFunctionRepository extends JpaRepository<SystemUserRoleFunction, Long> {

	@Modifying
	@Transactional
	@Query(value = "select F.functionId from SystemUserRoleFunction F where F.systemUserRoleId = :roleId ")
	public List<String> findRoleFunctionList(@Param("roleId") String roleId);
}
