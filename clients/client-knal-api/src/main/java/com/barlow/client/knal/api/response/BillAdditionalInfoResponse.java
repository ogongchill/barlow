package com.barlow.client.knal.api.response;

import java.util.List;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillAdditionalInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	BillAdditionalInfoBody body
) {
	public record BillAdditionalInfoBody(
		@JacksonXmlProperty(localName = "commMemo")
		List<CommMemoItem> commMemo,
		@JacksonXmlElementWrapper(localName = "exhaust")
		List<ExhaustItem> exhaust,
		@JacksonXmlProperty(localName = "billGbnCd")
		List<BillGbnCdItem> billGbnCdItem,
		@JacksonXmlProperty(localName = "bpmOthers")
		String bpmOthers // 도대체 무슨 항목인지 모르겠음. 문서에도 안나와있지만 응답되는 항목
	) {
	}

	public record CommMemoItem(
		String commMemo // 비고
	) {
	}

	public record ExhaustItem(
		String billLink, // 대안반영폐기 링크 경로
		String billName // 대안반영폐기 의안명
	) {
	}

	public record BillGbnCdItem(
		String billLink, // 대안링크경로
		String billName // 대안 명
	) {
	}
}

