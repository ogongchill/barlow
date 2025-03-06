package com.barlow.client.knal.opencongress.api;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

public class OpenCongressResponseDecoder implements Decoder {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		return OBJECT_MAPPER.readValue(getResponseBody(response), OBJECT_MAPPER.constructType(type));
	}

	private String getResponseBody(Response response) throws IOException {
		Response.Body body = response.body();
		Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
		return Util.toString(bodyReader);
	}
}
