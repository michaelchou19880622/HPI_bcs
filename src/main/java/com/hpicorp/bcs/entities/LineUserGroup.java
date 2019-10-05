package com.hpicorp.bcs.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "lineusergroup")
public class LineUserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@JsonIgnore
	@Column(name = "getusers")
	private String getusers;

	@Column(name = "modify_account")
	private String modifyAccount;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "modify_time")
	private Date modifyTime;

    @Column(name="system_flag")
    private String systemFlag;
	
}
