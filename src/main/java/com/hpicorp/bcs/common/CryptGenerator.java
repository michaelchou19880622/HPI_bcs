package com.hpicorp.bcs.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptGenerator {

	private CryptGenerator() {
	}
	
	public static String sha256(String key, String body) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKey);
			byte[] source = body.getBytes("UTF-8");
			return Base64.encodeBase64String(mac.doFinal(source));
		} catch (Exception e) {
			return null;
		}
	}
}
