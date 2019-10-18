package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageCarouselAction;

@Repository
public interface MessageCarouselActionRepository extends JpaRepository<MessageCarouselAction, Long> {

}
