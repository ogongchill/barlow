package com.barlow.client.knal.opendata.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import feign.QueryMapEncoder;
import feign.codec.EncodeException;

public class KnalEncoder implements QueryMapEncoder {

	@Override
	public Map<String, Object> encode(Object object) {
		try {
			Map<String, Object> result = new HashMap<>();
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(object);
				if (value != null) {
					String key = getJsonPropertyValue(field);
					result.put(key, value);
				}
			}
			return result;
		} catch (IllegalAccessException e) {
			throw new EncodeException("Failed to encode query parameters", e);
		}
	}

	private String getJsonPropertyValue(Field field) {
		JsonProperty annotation = field.getAnnotation(JsonProperty.class);
		if (annotation != null && !annotation.value().isEmpty()) {
			return annotation.value(); // @JsonProperty가 있으면 해당 값(snake_case) 사용
		}
		return field.getName(); // 없으면 기본 필드명(camelCase) 유지
	}
}
