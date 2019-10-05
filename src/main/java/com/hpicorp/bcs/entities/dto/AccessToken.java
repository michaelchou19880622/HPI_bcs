package com.hpicorp.bcs.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class AccessToken {

	public final String scope;

	public final String access_token;
	
	public final String token_type;
	
	public final Integer expires_in;
	
	public final String refresh_token;
	
	public final String id_token;

}
