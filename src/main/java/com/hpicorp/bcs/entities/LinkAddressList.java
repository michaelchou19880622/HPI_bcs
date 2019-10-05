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
@ToString(exclude = "linkAddress")
@Table(name = "linkaddresslist")
public class LinkAddressList implements Serializable {

	private static final long serialVersionUID = 8673328848208795823L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "link_id", insertable = false, updatable = false)
	private Long linkId;

	@Column(name = "title")
	private String title;

	@Column(name = "url")
	private String url;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "tag")
	private String tag;

	@Column(name = "type")
	private String type;
	
	@ManyToOne
	@JoinColumn(name = "link_id")
	@JsonIgnore
	private LinkAddress linkAddress;
	
}
