package com.hpicorp.bcs.enums;

public enum MessageActionTypes {
	
	POSTBACK("postback"),
	MESSAGE("message"),
	URI("uri");

	private String value;
	
	private MessageActionTypes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}

	
