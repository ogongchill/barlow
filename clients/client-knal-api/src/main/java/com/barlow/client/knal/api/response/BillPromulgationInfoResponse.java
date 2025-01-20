package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.barlow.client.knal.api.response.common.ItemListPagingBody;
import com.barlow.client.knal.api.response.item.BillPromulgationInfoItem;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillPromulgationInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	ItemListPagingBody<BillPromulgationInfoItem> body
) {
	@Override
	public String toString() {
		String linedSeparator = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("header.code: ").append(header.getCode()).append(linedSeparator)
			.append("header.message: ").append(header.getMessage()).append(linedSeparator)
			.append("header.requestMsgId: ").append(header.getRequestMsgID()).append(linedSeparator)
			.append("header.responseMsgId: ").append(header.getResponseMsgID()).append(linedSeparator)
			.append("header.responseTime: ").append(header.getResponseTime()).append(linedSeparator)
			.append("header.successYN: ").append(header.getSuccessYN()).append(linedSeparator);
		if (body != null) {
			builder.append("body.pageNo: ").append(body.getPageNo()).append(linedSeparator)
				.append("body.numOfRows: ").append(body.getNumOfRows()).append(linedSeparator)
				.append("body.totalCount: ").append(body.getTotalCount()).append(linedSeparator);
			for (BillPromulgationInfoItem item : body.getItems()) {
				builder.append(item).append(linedSeparator);
			}
		}
		return builder.toString();
	}
}
