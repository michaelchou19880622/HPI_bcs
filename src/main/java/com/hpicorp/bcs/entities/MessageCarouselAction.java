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
@ToString(exclude = "messageCarouselColumn")
@Table(name = "message_carousel_action")
public class MessageCarouselAction implements Serializable {

	private static final long serialVersionUID = 5649328268897751368L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "column_id", insertable = false, updatable = false)
	private Long columnId;

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
    @JoinColumn(name = "column_id")
	@JsonIgnore
    private MessageCarouselColumn messageCarouselColumn;	

}
