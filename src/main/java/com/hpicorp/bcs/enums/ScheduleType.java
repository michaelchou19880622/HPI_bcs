package com.hpicorp.bcs.enums;

public enum ScheduleType {

	RICH_MENU_GROUP("RICH_MENU_GROUP");
	
	private String value;
	
	private ScheduleType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
