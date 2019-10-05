package com.hpicorp.bcs.enums;

public enum RoleName {
	
	ROLE000001("Role000001"),
	ROLE000002("Role000002"),
	ROLE000003("Role000003");
	
	private String value;
	
	private RoleName(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public static RoleName fromString(String text) {
		for (RoleName b : RoleName.values()) {
			if (b.value.equalsIgnoreCase(text)) {
				return b;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}
	
}
