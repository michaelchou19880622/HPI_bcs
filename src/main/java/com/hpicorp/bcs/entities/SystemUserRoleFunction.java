package com.hpicorp.bcs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "systemuserrole_function")
public class SystemUserRoleFunction implements Serializable {

	private static final long serialVersionUID = 4483069692230883293L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 10)
	@Column(name = "systemuserrole_id")
	private String systemUserRoleId;

	@Size(max = 10)
	@Column(name = "function_id")
	private String functionId;

}
