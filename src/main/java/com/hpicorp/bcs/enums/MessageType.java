package com.hpicorp.bcs.enums;

public enum MessageType {
	
	FLEX("FLEX"),
	TEXT("TEXT"), 
	STICKER("STICKER"), 
	IMAGE("IMAGE"), 
	VIDEO("VIDEO"), 
	AUDIO("AUDIO"), 
	LINK("LINK"), 
	LOCATION("LOCATION"), 
	TEMPLATE("TEMPLATE"), 
	CAROUSEL("CAROUSEL"), 
	IMAGEMAP("IMAGEMAP");
	
	private String value;

	private MessageType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static MessageType fromString(String text) {
		for (MessageType messagetype : MessageType.values()) {
			if (messagetype.value.equalsIgnoreCase(text)) {
				return messagetype;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}

	@Override
	public String toString() {
		return value;
	}
}
