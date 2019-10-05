package com.hpicorp.bcs.enums;

public enum ApplyDataInputStatus {
	
	YES("Y"),
	NO("N");
	
	private String value;

	private ApplyDataInputStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static ApplyDataInputStatus fromString(String text) {
	    for (ApplyDataInputStatus b : ApplyDataInputStatus.values()) {
	      if (b.value.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    throw new IllegalArgumentException("No constant with text " + text + " found");
	  }
	
}
