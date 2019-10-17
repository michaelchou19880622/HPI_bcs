package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hpicorp.bcs.common.CustomerDateAndTimeDeserialize;

import lombok.Data;

@Data
@Entity
@Table(name = "message_imagemap")
public class MessageImageMap implements Serializable {

	private static final long serialVersionUID = -6905398776250212751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "base_url")
	private String baseUrl;

	@Column(name = "alt_text")
	private String altText;

	@Column(name = "base_size_width")
	private Integer baseSizeWidth;

	@Column(name = "base_size_height")
	private Integer baseSizeHeight;

	@Column(name = "type")
	private String type;

	@Column(name = "modify_account")
	private String modifyAccount;

	@LastModifiedDate
	@JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
	@Column(name = "modify_time")
	private Date modifyTime;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "messageImageMap", orphanRemoval = true)
	private List<MessageImageMapAction> messageImageMapActionList = new ArrayList<>();

}
