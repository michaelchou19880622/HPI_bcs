package com.hpicorp.bcs.enums;

public enum LineUserBindStatus {

	ALL("A"), 
	BINDED("Y"), 
	UNBINDED("N"),
	STATUS_BINDED("BINDED"),
	STATUS_UNBIND("UNBIND");

	private String values;

	private LineUserBindStatus(String values) {
		this.values = values;
	}

	public String getValues() {
		return this.values;
	}

	public static LineUserBindStatus fromString(String text) {
		for (LineUserBindStatus b : LineUserBindStatus.values()) {
			if (b.values.equalsIgnoreCase(text)) {
				return b;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}

}
