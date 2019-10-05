package com.hpicorp.bcs.enums;

public enum UserClickType {

	MESSAGE("MESSAGE"),
	AUTOREPLY("AUTOREPLY"),
	LINK("LINK"),
	TAG("TAG");
	
	private String values;

	private UserClickType(String values) {
		this.values = values;
	}
	
	public String getValues() {
		return this.values;
	}
	
	public static UserClickType fromString(String text) {
	    for (UserClickType b : UserClickType.values()) {
	      if (b.values.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    throw new IllegalArgumentException("No constant with text " + text + " found");
	  }
	
}
