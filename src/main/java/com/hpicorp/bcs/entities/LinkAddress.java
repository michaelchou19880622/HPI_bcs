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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "linkaddress")
public class LinkAddress implements Serializable {

	private static final long serialVersionUID = -4877235679970819009L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "url")
	private String url;

	@Column(name = "modify_user")
	private String modifyUser;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "modify_time")
	private Date modifyTime;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "linkAddress", orphanRemoval = true)
	private List<LinkAddressList> linkAddressList = new ArrayList<>();

}
