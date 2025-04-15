package com.barlow.client.knal.opendata.api.response.common;

public interface OpenDataResponse<T> {

	Header header();

	Body<T> body();

	default String toStringForLog() {
		String linedSeparator = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		Header header = header();
		if (header != null) {
			builder.append("header.resultCode: ").append(header.resultCode()).append(linedSeparator)
				.append("header.resultMsg: ").append(header.resultMsg()).append(linedSeparator);
		}
		Body<T> body = body();
		if (body != null) {
			builder.append("body.pageNo: ").append(body.pageNo()).append(linedSeparator)
				.append("body.numOfRows: ").append(body.numOfRows()).append(linedSeparator)
				.append("body.totalCount: ").append(body.totalCount()).append(linedSeparator);
			for (T item : body.items()) {
				builder.append(item).append(linedSeparator);
			}
		}
		return builder.toString();
	}
}
