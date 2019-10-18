package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageCarouselTemplate;

@Repository
public interface MessageCarouselTemplateRepository extends JpaRepository<MessageCarouselTemplate, Long> {

}
