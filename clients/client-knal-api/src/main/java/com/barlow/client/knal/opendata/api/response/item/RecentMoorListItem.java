package com.barlow.client.knal.opendata.api.response.item;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record RecentMoorListItem(
	@JacksonXmlProperty(localName = "billid") String billId, // 의안 ID
	@JacksonXmlProperty(localName = "billno") String billNo, // 의안번호
	@JacksonXmlProperty(localName = "billname") String billName, // 의안명
	@JacksonXmlProperty(localName = "proposer") String proposer, // 제안자
	@JacksonXmlProperty(localName = "proposerkind") String proposerKind, // 제안자구분
	@JacksonXmlProperty(localName = "proposedt") String proposeDt, // 제안일
	@JacksonXmlProperty(localName = "submitdt") String submitDt, // 회부일
	@JacksonXmlProperty(localName = "committeename") String committeeName // 소관위원회
) {
}
