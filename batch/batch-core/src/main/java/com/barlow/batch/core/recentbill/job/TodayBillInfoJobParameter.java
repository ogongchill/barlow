package com.barlow.batch.core.recentbill.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;

import com.barlow.batch.core.recentbill.client.TodayBillInfoResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TodayBillInfoJobParameter extends JobParameter<TodayBillInfoResult> {

	private static final Logger log = LoggerFactory.getLogger(TodayBillInfoJobParameter.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public TodayBillInfoJobParameter(TodayBillInfoResult value) {
		super(value, TodayBillInfoResult.class);
	}

	public static TodayBillInfoResult fromString(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, TodayBillInfoResult.class);
		} catch (JsonProcessingException e) {
			log.warn("");
			throw new RuntimeException("Failed to deserialize JSON to TodayBillInfoResult", e);
		}
	}

	@Override
	public String toString() {
		try {
			return OBJECT_MAPPER.writeValueAsString(getValue());
		} catch (JsonProcessingException e) {
			log.warn("");
			throw new RuntimeException("Failed to serialize TodayBillInfoResult to JSON", e);
		}
	}
}