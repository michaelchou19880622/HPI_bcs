package com.hpicorp.bcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.MessageAudio;

@Repository
public interface MessageAudioRepository extends JpaRepository<MessageAudio, Long> {

}
