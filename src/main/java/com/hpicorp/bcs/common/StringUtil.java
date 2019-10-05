package com.hpicorp.bcs.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	/**
	 * 進行字串base64解碼
	 * @param data
	 * @return
	 */
	public static String decodeBase64(String data){
		String result = "";
		
		try {
			result = new String(Base64.decodeBase64(data.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		
		return result;
	}
}
