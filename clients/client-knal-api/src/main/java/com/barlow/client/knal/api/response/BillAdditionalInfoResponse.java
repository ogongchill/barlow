package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillAdditionalInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	BillAdditionalInfoBody body
) {
}

