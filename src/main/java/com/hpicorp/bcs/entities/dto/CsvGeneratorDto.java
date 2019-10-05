package com.hpicorp.bcs.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvGeneratorDto {

	private List<String> headers;
	
	private List<List<Object>> rows;
	
}
