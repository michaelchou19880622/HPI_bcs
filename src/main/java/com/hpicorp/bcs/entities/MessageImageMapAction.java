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
@ToString(exclude = "messageImageMap")
@Table(name = "message_imagemap_action")
public class MessageImageMapAction implements Serializable {

	private static final long serialVersionUID = 763335216538577503L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "message_id", insertable = false, updatable = false)
	private Long messageId;

	@Column(name = "type")
	private String type;

	@Column(name = "label")
	private String label;

	@Column(name = "link_uri")
	private String linkUri;

	@Column(name = "text")
	private String text;

	@Column(name = "area_x")
	private Integer areaX;

	@Column(name = "area_y")
	private Integer areaY;

	@Column(name = "area_width")
	private Integer areaWidth;

	@Column(name = "area_height")
	private Integer areaHeight;

	@ManyToOne
	@JoinColumn(name = "message_id")
	@JsonIgnore
	private MessageImageMap messageImageMap;
	
}
