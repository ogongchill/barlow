package com.barlow.client.knal.opendata.api.response.common;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public final class GenericItemBody<T> implements Body<T> {

	@JacksonXmlProperty(localName = "body")
	private T body;

	@Override
	public T body() {
		return body;
	}

	@Override
	public Integer numOfRows() {
		return null;
	}

	@Override
	public Integer pageNo() {
		return null;
	}

	@Override
	public Integer totalCount() {
		return null;
	}

	@Override
	public List<T> items() {
		return List.of();
	}

	@Override
	public T item() {
		return null;
	}
}
