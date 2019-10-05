package com.hpicorp.bcs.common;

import java.util.ArrayList;
import java.util.List;

public class ListExtension {

	private ListExtension() {
		
	}
	
	public static <T> List<List<T>> chopped(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<>();
	    if (list == null || list.isEmpty()) {
	    	return parts;
	    }
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
	
	public static <T> T first(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
}
