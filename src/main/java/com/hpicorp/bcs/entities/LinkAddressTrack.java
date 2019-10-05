package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "linkaddress_track")
public class LinkAddressTrack implements Serializable {

	private static final long serialVersionUID = 2492555473682386525L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lineuser_id")
	private Long lineUserId;

	@Column(name = "linkaddress_id")
	private Long linkAddressId;

	@Column(name = "linkaddresslist_id")
	private Long linkAddressListId;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "create_time", nullable = false, updatable = false)
	private Date createTime;

}
