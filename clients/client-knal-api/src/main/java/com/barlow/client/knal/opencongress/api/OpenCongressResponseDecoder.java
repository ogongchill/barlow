package com.barlow.client.knal.opencongress.api;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barlow.client.knal.opencongress.api.common.Head;
import com.barlow.client.knal.opencongress.api.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

public class OpenCongressResponseDecoder implements Decoder {

	private static final Logger log = LoggerFactory.getLogger(OpenCongressResponseDecoder.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final String OPEN_CONGRESS_ERROR_RESPONSE_FORMAT = "openCongress 에러 JSON 응답 : %s - %s";

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		String responseBody = getResponseBody(response);
		if (ErrorCode.isNameContainedInValue(responseBody)) {
			Head.Result result = OBJECT_MAPPER.readValue(responseBody, Head.Result.class);
			String errorMessage = String.format(OPEN_CONGRESS_ERROR_RESPONSE_FORMAT, result.code(), result.message());
			log.info(errorMessage);
			throw new OpenCongressException(errorMessage);
		}
		return OBJECT_MAPPER.readValue(responseBody, OBJECT_MAPPER.constructType(type));
	}

	private String getResponseBody(Response response) throws IOException {
		Response.Body body = response.body();
		Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
		return Util.toString(bodyReader);
	}
}
