package com.hpicorp.bcs.common;

import java.util.Optional;

public class OptionalExtension {

	private OptionalExtension() {
	}

	public static <T> T get(Optional<T> optional) {
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
}
