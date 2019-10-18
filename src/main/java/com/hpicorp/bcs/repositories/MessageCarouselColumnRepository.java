package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageCarouselColumn;

@Repository
public interface MessageCarouselColumnRepository extends JpaRepository<MessageCarouselColumn, Long> {
	
}
