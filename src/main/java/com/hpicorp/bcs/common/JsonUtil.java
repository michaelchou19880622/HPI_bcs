package com.hpicorp.bcs.common;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Slf4j
public class JsonUtil {
	
	private JsonUtil() {
	}

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String ERROR_MESSAGE = "Parse String to Object error => {}";  

	static {
		
		// 对象的所有字段全部列入
		objectMapper.setSerializationInclusion(Include.ALWAYS);

		// 取消默认转换timestamps形式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		// 忽略空Bean转json的错误
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// 所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// 忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
	}

	public static ObjectMapper getObjectMapper() {

		return objectMapper;
	}

	public static <T> String obj2String(T obj) {
		if (obj == null) {
			return null;
		}
		try {
			return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

	public static <T> String obj2StringPretty(T obj) {
		if (obj == null) {
			return null;
		}
		try {
			return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T string2Obj(String str, Class<T> clazz) {
		if (StringUtils.isEmpty(str) || clazz == null) {
			return null;
		}

		try {
			return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
		if (StringUtils.isEmpty(str) || typeReference == null) {
			return null;
		}
		try {
			return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

	public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return objectMapper.readValue(str, javaType);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

	public static JsonNode string2JsonNode(String str) {
		try {
			return objectMapper.readTree(str);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			return null;
		}
	}

}
