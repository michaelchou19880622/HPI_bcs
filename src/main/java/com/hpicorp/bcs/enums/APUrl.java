package com.hpicorp.bcs.enums;

public enum APUrl {

	SCHDULER("/service/scheduler/add?sendId=");
	
	private String path;

	private APUrl(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
}
