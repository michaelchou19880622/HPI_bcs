package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.MessageTemplateAction;

@Repository
public interface MessageTemplateActionRepository extends JpaRepository<MessageTemplateAction, Long> {

}
