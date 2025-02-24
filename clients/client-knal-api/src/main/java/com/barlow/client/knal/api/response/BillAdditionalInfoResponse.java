package com.barlow.client.knal.api.response;

import java.util.List;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.barlow.client.knal.api.response.item.BillGbnCdItem;
import com.barlow.client.knal.api.response.item.CommMemoItem;
import com.barlow.client.knal.api.response.item.ExhaustItem;
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
}

