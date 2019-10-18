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

import lombok.Data;

@Data
@Entity
@Table(name = "send_message")
public class SendMessage implements Serializable {

	private static final long serialVersionUID = -8401000315774415663L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mode")
	private String mode;

	@Column(name = "mode_week")
	private String modeWeek;

	@Column(name = "mode_day")
	private Integer modeDay;

	@Column(name = "mode_hour")
	private Integer modeHour;

	@Column(name = "mode_min")
	private Integer modeMin;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "schedule")
	@Temporal(TemporalType.TIMESTAMP)
	private Date schedule;

	@Column(name = "category")
	private String category;

	@Column(name = "lineuser_count")
	private Integer count;

	@Column(name = "lineuser_ok_count")
	private Integer lineUserOkCount;

	@Column(name = "status")
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time")
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time")
	private Date modifyTime;

	@Column(name = "create_account")
	private String createAccount;
	
	@Column(name = "modify_account")
	private String modifyAccount;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "sendMessage", orphanRemoval = true)
	private List<SendMessageList> sendMessageList = new ArrayList<>();

}
