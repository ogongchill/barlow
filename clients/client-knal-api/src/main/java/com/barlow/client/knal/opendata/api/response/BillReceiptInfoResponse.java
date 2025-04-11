package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.client.knal.opendata.api.response.common.SingleItemBody;
import com.barlow.client.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.client.knal.opendata.api.response.item.BillReceiptInfoItem;

public record BillReceiptInfoResponse(
	SuccessHeader header,
	SingleItemBody<BillReceiptInfoItem> body
) implements OpenDataResponse<BillReceiptInfoItem> {

	@Override
	public String toString() {
		String linedSeparator = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("header.resultCode: ").append(header.resultCode()).append(linedSeparator)
			.append("header.resultMsg: ").append(header.resultMsg()).append(linedSeparator)
			.append("header.requestMsgId: ").append(header.requestMsgID()).append(linedSeparator)
			.append("header.responseMsgId: ").append(header.requestMsgID()).append(linedSeparator)
			.append("header.responseTime: ").append(header.responseTime()).append(linedSeparator)
			.append("header.successYN: ").append(header.successYN()).append(linedSeparator);
		if (body != null) {
			builder.append("body: ").append(body.item().toString()).append("\n");
		}
		return builder.toString();
	}
}
