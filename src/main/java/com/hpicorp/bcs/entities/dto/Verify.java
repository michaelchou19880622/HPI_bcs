package com.hpicorp.bcs.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Verify {

	public final String scope;

	public final String client_id;
	
	public final Integer expires_in;

}
