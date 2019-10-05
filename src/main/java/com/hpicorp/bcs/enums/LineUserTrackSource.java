package com.hpicorp.bcs.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LineUserTrackSource {
	
	UNBINDED("UNBINDED"),
	BINDED("BINDED"),
	FOLLOW("NORMALLY"),
	BLOCK("BLOCK");
	
	private String values;

	private LineUserTrackSource(String value) {
		this.values = value;
	}
	
	@Override
	public String toString() {
		return this.values;
	}
	
	public static LineUserTrackSource fromString(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text Can't be Null");
		}
	    for (LineUserTrackSource b : LineUserTrackSource.values()) {
	      if (b.values.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    throw new IllegalArgumentException("No constant with text " + text + " found");
	  }
	
	public static List<LineUserTrackSource> getListByString(String source) {
		List<LineUserTrackSource> returnSource = new ArrayList<>();
		try {
			LineUserTrackSource casts = LineUserTrackSource.fromString(source);
			return Arrays.asList(casts);
		} catch (Exception e) {
//			cast Error
		}
		for (LineUserTrackSource b : LineUserTrackSource.values()) {
			returnSource.add(b);
		}
		return returnSource;
	}
	
	public static LineUserTrackSource convert(LineUserStatus status)  {
		try {
			return LineUserTrackSource.fromString(status.toString());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public static LineUserTrackSource convert(LineUserBindStatus status)  {
		try {
			return LineUserTrackSource.fromString(status.toString());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
