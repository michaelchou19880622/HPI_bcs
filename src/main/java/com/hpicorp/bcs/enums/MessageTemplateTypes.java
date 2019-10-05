package com.hpicorp.bcs.enums;

public enum MessageTemplateTypes {

	BUTTONS("buttons"), 
	CONFIRM("confirm");

	private String value;

	private MessageTemplateTypes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
