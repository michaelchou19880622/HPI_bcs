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
@Table(name = "message_sticker")
@JsonTypeName("sticker")
public class MessageSticker implements Serializable {

	private static final long serialVersionUID = 59594411889406733L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "sticker_id", nullable = false)
	private String stickerId;

	@Column(name = "package_id", nullable = false)
	private String packageId;

}
