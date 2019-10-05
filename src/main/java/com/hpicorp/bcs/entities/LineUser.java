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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "lineuser")
public class LineUser implements Serializable {

	private static final long serialVersionUID = 3128292317717677851L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "line_uid", nullable = false, length = 33, unique = true)
	private String lineUid;
	
	@Column(name = "gid")
	private String gid;
	
	@Column(name = "private_code")
	private String privateCode;
	
	@Column(name = "s_private_code")
	private String sPrivateCode;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image")
	private String image;

	@Column(name = "status")
	private Integer status;
	
	@Column(name = "linked")
    private String linked;

	@Column(name = "tag")
	private String tag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	@CreatedDate
	private Date createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time")
	@LastModifiedDate
	private Date modifyTime;

}
