package com.hpicorp.bcs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpicorp.bcs.entities.SystemUserRole;

public interface SystemUserRoleRepository extends JpaRepository<SystemUserRole, String> {

	Optional<SystemUserRole> findByName(String name);

}
