package com.hpicorp.bcs.entities.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoreplyResultBody {

	private String keyword;
	
	private Date period;
	
	private Integer page;
	
}
