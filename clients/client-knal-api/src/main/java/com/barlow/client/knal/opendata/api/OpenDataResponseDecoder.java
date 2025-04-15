package com.barlow.client.knal.opendata.api;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barlow.client.knal.opendata.api.code.ErrorCode;
import com.barlow.client.knal.opendata.api.response.error.ErrorResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

class OpenDataResponseDecoder implements Decoder {

	private static final Logger log = LoggerFactory.getLogger(OpenDataResponseDecoder.class);
	private static final XmlMapper XML_MAPPER = new XmlMapper();
	private static final String OPEN_DATA_ERROR_RESPONSE_FORMAT = "openData 에러 XML 응답 : %s";

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		String responseBody = getResponseBody(response);
		if (ErrorCode.isNameContainedInValue(responseBody)) {
			ErrorResponse errorResponse = XML_MAPPER.readValue(responseBody, ErrorResponse.class);
			ErrorCode errorCode = ErrorCode.valueOf(errorResponse.header().returnAuthMsg());
			String errorMessage = String.format(OPEN_DATA_ERROR_RESPONSE_FORMAT, errorCode.name());
			log.info(errorMessage);
			throw new OpenDataException(errorMessage);
		}
		return XML_MAPPER.readValue(
			responseBody,
			XML_MAPPER.constructType(type)
		);
	}

	private String getResponseBody(Response response) throws IOException {
		Response.Body body = response.body();
		Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
		return Util.toString(bodyReader);
	}
}
