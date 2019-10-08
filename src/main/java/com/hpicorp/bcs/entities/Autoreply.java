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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hpicorp.bcs.common.CustomerDateAndTimeDeserialize;

import lombok.Data;

@Data
@Entity
@Table(name = "autoreply")
public class Autoreply implements Serializable {

	private static final long serialVersionUID = 3362166456415881056L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "keyword")
	private String keyword;

	@Column(name = "type")
	private String type;

	@Column(name = "status")
	private String status;

	@Column(name = "user_status")
	private String userStatus;

	@Column(name = "reply_index")
	private Integer replyIndex;

	@Column(name = "period")
	private String period;

	@JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
	@Column(name = "datetime_begin")
	private Date datetimeBegin;

	@JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
	@Column(name = "datetime_end")
	private Date datetimeEnd;

	@Column(name = "response_count")
	private Integer responseCount;

	@Column(name = "memo")
	private String memo;

	@Column(name = "lineusergroup_id")
	private Long lineusergroupId;

//	@Column(name = "creation_time", nullable = false, insertable = false, updatable = false)
	@Column(name = "creation_time", nullable = false, updatable = false)
	private Date creationTime;

//	@Column(name = "modification_time", nullable = false, insertable = false, updatable = false)
	@Column(name = "modification_time", nullable = false, updatable = false)
	private Date modificationTime;

	@Column(name = "modify_user")
	private String modifyUser;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "autoreply", orphanRemoval = true)
	private List<AutoreplyDetail> autoreplyDetails = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "autoreply", orphanRemoval = true)
	private List<AutoreplyMessageList> autoreplyMessageList = new ArrayList<>();
	
	@PrePersist
	public void setCreationTimeWhileInsert() {
		this.creationTime = new Date();
	}
	
	@PreUpdate
	public void setModificationTimeWhileUpdate() {
		this.modificationTime = new Date();
	}

}
