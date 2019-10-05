package com.hpicorp.bcs.enums;

public enum ResponseStatus {

	INSERT_SUCCESS("insert complete"),
	UPDATE_SUCCESS("update complete"),
	DELETE_SUCCESS("delete complete"),
	ERROR("error = {}"),
	CHECK_PASS("pass checked"),
	EXCHANGE_SUCCESS("exchange success"),
	SUCCESS_CODE("200");
	
	
	private String value;
	
	private ResponseStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public ResponseStatus fromString(String text) {
		for (ResponseStatus rs : ResponseStatus.values()) {
			if (rs.value.equalsIgnoreCase(text)) {
				return rs;
			}
		}
		throw new IllegalArgumentException("沒有符合此文字 = " + text);
	}
	
}
