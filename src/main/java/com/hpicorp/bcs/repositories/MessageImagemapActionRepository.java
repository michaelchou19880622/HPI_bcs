package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageImageMapAction;

@Repository
public interface MessageImagemapActionRepository extends JpaRepository<MessageImageMapAction, Long> {
	
}
