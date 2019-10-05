package com.hpicorp.bcs.enums;

public enum SendMessageMode {

	NOW("NOW"), 
	ONCE("ONCE"), 
	EVERYDAY("EVERYDAY"), 
	MONTHLY("MONTHLY"), 
	WEEKLY("WEEKLY");

	private String value;

	private SendMessageMode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
