package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.barlow.client.knal.api.response.common.ItemListBody;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillPreLiminaryExaminationInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	ItemListBody<BillPreLiminaryExaminationItem> body
) {
	public record BillPreLiminaryExaminationItem(
		String comitName,
		String meetingInfoLink,
		String presentDt,
		String procDt,
		String submitDt
	) {
	}
}
