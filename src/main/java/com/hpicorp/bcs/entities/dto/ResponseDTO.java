package com.hpicorp.bcs.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

	private Integer statusCode;
	
	private String responseBody;
	
}
