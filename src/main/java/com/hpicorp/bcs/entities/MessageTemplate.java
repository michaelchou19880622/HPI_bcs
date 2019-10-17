package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "message_template")
public class MessageTemplate implements Serializable {

	private static final long serialVersionUID = -9184769987817782942L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "alt_text")
	private String altText;

	@Column(name = "title")
	private String title;

	@Column(name = "text")
	private String text;

	@Column(name = "thumbnail_image_url")
	private String thumbnailImageUrl;

	@Column(name = "image_aspect_ratio")
	private String imageAspectRatio;

	@Column(name = "image_size")
	private String imageSize;

	@Column(name = "image_background_color")
	private String imageBackgroundColor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_datetime")
	private Date modifyDatetime;

	@Column(name = "modify_user")
	private String modifyUser;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "messageTemplate", orphanRemoval = true)
	private List<MessageTemplateAction> messageTemplateActionList;

}
