package com.barlow.client.knal.opendata.api;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

class OpenDataResponseDecoder implements Decoder {

	private static final XmlMapper XML_MAPPER = new XmlMapper();

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		return XML_MAPPER.readValue(
			getResponseBody(response),
			XML_MAPPER.constructType(type)
		);
	}

	private String getResponseBody(Response response) throws IOException {
		Response.Body body = response.body();
		Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
		return Util.toString(bodyReader);
	}
}
