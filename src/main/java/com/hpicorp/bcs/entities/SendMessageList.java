package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "sendMessage")
@Table(name = "send_message_list")
public class SendMessageList implements Serializable {

	private static final long serialVersionUID = -5389345752156661395L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "send_id", insertable = false, updatable = false)
	private Long sendId;

	@Column(name = "message_id")
	private Long messageId;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "order_index")
	private Integer orderIndex;

	@ManyToOne
	@JoinColumn(name = "send_id")
	@JsonIgnore
	private SendMessage sendMessage;

	@Transient
	private List<MessageText> messageTextList = new ArrayList<>();

	@Transient
	private List<MessageImage> messageImageList = new ArrayList<>();

	@Transient
	private List<MessageVideo> messageVideoList = new ArrayList<>();

	@Transient
	private List<MessageAudio> messageAudioList = new ArrayList<>();

	@Transient
	private List<MessageSticker> messageStickerList = new ArrayList<>();

	@Transient
	private List<MessageImageMap> messageImageMapList = new ArrayList<>();

	@Transient
	private List<MessageTemplate> messageTemplateList = new ArrayList<>();
}
