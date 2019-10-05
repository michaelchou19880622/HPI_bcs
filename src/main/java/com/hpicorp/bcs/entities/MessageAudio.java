package com.hpicorp.bcs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

@Data
@Entity
@Table(name = "message_audio")
@JsonTypeName("audio")
public class MessageAudio implements Serializable {

	private static final long serialVersionUID = -1869162878853016544L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "original_content_url", nullable = false)
	private String originalContentUrl;

	@Column(name = "duration", nullable = false)
	private Integer duration;

}
