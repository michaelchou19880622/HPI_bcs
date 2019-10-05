package com.hpicorp.bcs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpicorp.bcs.entities.RichMenuList;

public interface RichMenuListRepository extends JpaRepository<RichMenuList, Long> {

	public Optional<RichMenuList> findByRichMenuId(String richMenuId);
	
}
