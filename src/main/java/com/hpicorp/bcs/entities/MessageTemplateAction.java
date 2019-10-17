package com.hpicorp.bcs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "message_template_action")
@ToString(exclude = "messageTemplate")
public class MessageTemplateAction implements Serializable {

	private static final long serialVersionUID = -8880919110334308619L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "template_id", insertable = false, updatable = false)
	private Long templateId;

	@Column(name = "template_type")
	private String templateType;

	@Column(name = "type")
	private String type;

	@Column(name = "label")
	private String label;

	@Column(name = "data")
	private String data;

	@Column(name = "text")
	private String text;

	@Column(name = "uri")
	private String uri;

	@ManyToOne
	@JoinColumn(name = "template_id")
	@JsonIgnore
	private MessageTemplate messageTemplate;

}
