package com.hpicorp.bcs.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LinkAddressType {

	DEFAULT("DEFAULT"), 
	BIND("BIND");

	private String values;

	private LinkAddressType(String value) {
		this.values = value;
	}

	@Override
	public String toString() {
		return this.values;
	}

	public static LinkAddressType fromString(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text Can't be Null");
		}
		for (LinkAddressType b : LinkAddressType.values()) {
			if (b.values.equalsIgnoreCase(text)) {
				return b;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}

	public static List<LinkAddressType> getListByString(String source) {
		List<LinkAddressType> returnSource = new ArrayList<>();
		try {
			LinkAddressType casts = LinkAddressType.fromString(source);
			return Arrays.asList(casts);
		} catch (Exception e) {
		}
		for (LinkAddressType b : LinkAddressType.values()) {
			returnSource.add(b);
		}
		return returnSource;
	}

	public static LinkAddressType convert(LineUserStatus status) {
		try {
			return LinkAddressType.fromString(status.toString());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static LinkAddressType convert(LineUserBindStatus status) {
		try {
			return LinkAddressType.fromString(status.toString());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
