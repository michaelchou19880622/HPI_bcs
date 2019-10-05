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
@Table(name = "message_text")
@JsonTypeName("text")
public class MessageText implements Serializable {

	private static final long serialVersionUID = 6888901839908128847L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "text", nullable = true)
	private String text;

}
