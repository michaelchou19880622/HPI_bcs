package com.hpicorp.bcs.enums;

public enum DefaultConfig {
	
	LINEMSGSIZE(5),
	PAGESIZE(10);

	private Integer value;
	
	private DefaultConfig(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}
	
}

	
