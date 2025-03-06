package com.barlow.client.knal.opendata.api.response.item;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record SessionRequestListItem(
	@JacksonXmlProperty(localName = "billId") String billId, // 의안 ID
	@JacksonXmlProperty(localName = "billNo") String billNo, // 의안번호
	@JacksonXmlProperty(localName = "billName") String billName, // 의안명
	@JacksonXmlProperty(localName = "committeeName") String committeeName, // 소관위원회
	@JacksonXmlProperty(localName = "proposer") String proposer, // 제안자
	@JacksonXmlProperty(localName = "generalResult") String generalResult, // 위원회 의결 결과
	@JacksonXmlProperty(localName = "billHwpLink") String billHwpLink, // 의안원문: HWP 파일 경로
	@JacksonXmlProperty(localName = "billPdfLink") String billPdfLink, // 의안원문: PDF 파일 경로
	@JacksonXmlProperty(localName = "judgeHwpLink") String judgeHwpLink, // 심사보고서 HWP 파일 경로
	@JacksonXmlProperty(localName = "judgePdfLink") String judgePdfLink, // 심사보고서 PDF 파일 경로
	@JacksonXmlProperty(localName = "reviewHwpLink") String reviewHwpLink, // 검토보고서 HWP 파일 경로
	@JacksonXmlProperty(localName = "reviewPdflInk") String reviewPdfLink // 검토보고서 PDF 파일 경로
) {
}
