package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.barlow.client.knal.api.response.common.SingleItemBody;
import com.barlow.client.knal.api.response.item.BillReceiptInfoItem;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillReceiptInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	SingleItemBody<BillReceiptInfoItem> body
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
			builder.append("body: ").append(body.getItem().toString()).append("\n");
		}
		return builder.toString();
	}
}
