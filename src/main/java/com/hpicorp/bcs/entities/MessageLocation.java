package com.hpicorp.bcs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

@Data
@Entity
@Table(name = "message_location")
@JsonTypeName("location")
public class MessageLocation implements Serializable {

	private static final long serialVersionUID = 3670984260733990345L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

}
