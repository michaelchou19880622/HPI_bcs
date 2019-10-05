package com.hpicorp.bcs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "systemuserrole")
public class SystemUserRole implements Serializable  {

	private static final long serialVersionUID = 8005806192191936591L;

	@Id
	@Column(name="id")
	private String id;

	@NaturalId
    @Column(length = 20)
    private String name;

    public SystemUserRole(String name) {
        this.name = name;
    }
    
}
