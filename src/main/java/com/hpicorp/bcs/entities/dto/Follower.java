package com.hpicorp.bcs.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follower {
	
	private List<String> userIds;
	
	private String next;
	
}
