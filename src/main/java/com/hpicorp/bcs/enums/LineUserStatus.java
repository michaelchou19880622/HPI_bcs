package com.hpicorp.bcs.enums;

public enum LineUserStatus {
	
	NORMALLY(1),
	BLOCK(9);
	
	private Integer value;

	private LineUserStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	
}
