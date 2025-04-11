package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.client.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.client.knal.opendata.api.response.item.BillPromulgationInfoItem;

public record BillPromulgationInfoResponse(
	SuccessHeader header,
	ItemResponseBody<BillPromulgationInfoItem> body
) implements OpenDataResponse<BillPromulgationInfoItem> {

	@Override
	public String toString() {
		String linedSeparator = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("header.resultCode: ").append(header.resultCode()).append(linedSeparator)
			.append("header.resultMsg: ").append(header.resultMsg()).append(linedSeparator)
			.append("header.requestMsgId: ").append(header.requestMsgID()).append(linedSeparator)
			.append("header.responseMsgId: ").append(header.responseMsgID()).append(linedSeparator)
			.append("header.responseTime: ").append(header.responseTime()).append(linedSeparator)
			.append("header.successYN: ").append(header.successYN()).append(linedSeparator);
		if (body != null) {
			builder.append("body.pageNo: ").append(body.pageNo()).append(linedSeparator)
				.append("body.numOfRows: ").append(body.numOfRows()).append(linedSeparator)
				.append("body.totalCount: ").append(body.totalCount()).append(linedSeparator);
			for (BillPromulgationInfoItem item : body.items()) {
				builder.append(item).append(linedSeparator);
			}
		}
		return builder.toString();
	}
}
