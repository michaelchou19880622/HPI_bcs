package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "sendMessage")
@Table(name = "send_message_users")
public class SendMessageUsers implements Serializable {

	private static final long serialVersionUID = -2378333849964537965L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "send_id", insertable = false, updatable = false)
	private Long sendId;

	@Column(name = "line_user_id")
	private Long lineUserId;

	@Column(name = "status")
	private Integer status;

	@Column(name = "response_code")
	private Integer responseCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time")
	private Date sendTime;

	@ManyToOne
	@JoinColumn(name = "send_id")
	@JsonIgnore
	private SendMessage sendMessage;

}
