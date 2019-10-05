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
@Table(name = "message_video")
@JsonTypeName("video")
public class MessageVideo implements Serializable {

	private static final long serialVersionUID = 6646249687913868481L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "original_content_url", nullable = false)
	private String originalContentUrl;

	@Column(name = "preview_image_url", nullable = false)
	private String previewImageUrl;

}
