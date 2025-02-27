package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.barlow.client.knal.api.response.common.ItemListPagingBody;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillPromulgationInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	ItemListPagingBody<BillPromulgationInfoItem> body
) {
	public record BillPromulgationInfoItem(
		@JacksonXmlProperty(localName = "anounceDt") String announceDt, // 공포 일자
		@JacksonXmlProperty(localName = "anounceNo") String announceNo, // 공포 번호
		@JacksonXmlProperty(localName = "lawTitle") String lawTitle, // 공포 법률명
		@JacksonXmlProperty(localName = "lawBonUrl") @JsonInclude(JsonInclude.Include.NON_NULL) String lawBonUrl// 공포법률 Link url
	) {
	}

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
