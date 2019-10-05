package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "system_config")
public class SystemConfig implements Serializable {

	private static final long serialVersionUID = -2077444119189756772L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "config_key", nullable = false)
	private String configKey;

	@Column(name = "config_value", nullable = false)
	private String configValue;

	@Column(name = "modify_time", nullable = false)
	private Date modifyTime;

}
