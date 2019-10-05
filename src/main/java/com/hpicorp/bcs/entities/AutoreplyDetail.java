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
@ToString(exclude = "autoreply")
@Table(name = "autoreply_detail")
public class AutoreplyDetail implements Serializable {

	private static final long serialVersionUID = -3504882918427122763L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "autoreply_id", insertable = false, updatable = false)
	private Long autoreplyId;
	
	@Column(name = "keyword")
	private String keyword;

	@ManyToOne
	@JoinColumn(name = "autoreply_id")
	@JsonIgnore
	private Autoreply autoreply;

}
