package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "messageCarouselTemplate")
@Table(name = "message_carousel_column")
public class MessageCarouselColumn implements Serializable {

	private static final long serialVersionUID = 4804197063583511824L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "carousel_id", insertable = false, updatable = false)
	private Integer carouselId;

	@Column(name = "type")
	private String type;

	@Column(name = "title")
	private String title;

	@Column(name = "text")
	private String text;

	@Column(name = "thumbnail_image_url")
	private String thumbnailImageUrl;

	@Column(name = "image_background_color")
	private String imageBackgroundColor;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "messageCarouselColumn", orphanRemoval = true)
	private List<MessageCarouselAction> messageCarouselActionList = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "carousel_id")
	@JsonIgnore
	private MessageCarouselTemplate messageCarouselTemplate;

}
