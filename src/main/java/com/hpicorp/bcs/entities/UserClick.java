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

import lombok.Data;

@Data
@Entity
@Table(name = "user_click")
public class UserClick implements Serializable {

	private static final long serialVersionUID = -2259523065280685651L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lineuser_uid", nullable = false, length = 33)
	private String lineUserUid;

	@Column(name = "lineuser_status")
	private String lineUserStatus;

	@Column(name = "type")
	private String type;

	@Column(name = "subtype")
	private String subtype;

	@Column(name = "mapping_id")
	private Long mappingId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;

}
