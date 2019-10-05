package com.hpicorp.bcs.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "upload_uid")
public class UploadUid {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "group_id")
	private Long groupId;
	
	@Column(name = "filename")
	private String filename;
	
	@Column(name = "original_filename")
	private String originalFilename;

	@Column(name = "uid")
	private String uid;

}
