package com.hpicorp.bcs.enums;

public enum LiffUrl {

	QUESTIONNAIRE_URL("/bcsweb/static/questionnaire/questionnaire.html");
	
	private String value;
	
	private LiffUrl(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
