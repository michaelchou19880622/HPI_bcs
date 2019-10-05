package com.hpicorp.bcs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "systemuser")
public class SystemUser implements Serializable {
	
	private static final long serialVersionUID = -6897021363807840321L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "account")
	private String account;

	@NotBlank
	@Size(max = 50)
	@Column(name = "name")
	private String name;

	@NotBlank
	@Size(max = 70)
	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private String status;

	@Size(max = 33)
	@Column(name = "lineuser_uid", nullable = true)
	private String lineuserUid;

	@Column(name = "login_status",nullable = true)
	private String loginStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time")
	private Date loginTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false)
	private Date createTime;

	@Size(max = 20)
	@Column(name = "create_account")
	private String createAccount;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "modify_time", nullable = false, updatable = true)
	private Date modifyTime;

	@Size(max = 20)
	@Column(name = "modify_account")
	private String modifyAccount;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userroles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<SystemUserRole> roles = new HashSet<>();

	public SystemUser(String account, String name, String password) {
		this.account = account;
		this.name = name;
		this.password = password;
	}
	
}
