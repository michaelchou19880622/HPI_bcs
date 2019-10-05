package com.hpicorp.bcs.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RichMenuMultiple {

	private String richMenuId;
	
	private List<String> userIds;
	
}
