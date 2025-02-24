package com.barlow.client.knal.api.response.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public abstract class Response<T> {

	@JacksonXmlProperty(localName = "header")
	protected Header header;

	@JacksonXmlProperty(localName = "body")
	protected ItemListPagingBody<T> body;

	public Header getHeader() {
		return header;
	}

	public ItemListPagingBody<T> getBody() {
		return body;
	}

	@Override
	public String toString() {
		String linedSeparator = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		if (header != null) {
			builder.append("header.code: ").append(header.getCode()).append(linedSeparator)
				.append("header.message: ").append(header.getMessage()).append(linedSeparator);
		}
		if (body != null) {
			builder.append("body.pageNo: ").append(body.getPageNo()).append(linedSeparator)
				.append("body.numOfRows: ").append(body.getNumOfRows()).append(linedSeparator)
				.append("body.totalCount: ").append(body.getTotalCount()).append(linedSeparator);
			for (T item : body.getItems()) {
				builder.append(item).append(linedSeparator);
			}
		}
		return builder.toString();
	}
}
