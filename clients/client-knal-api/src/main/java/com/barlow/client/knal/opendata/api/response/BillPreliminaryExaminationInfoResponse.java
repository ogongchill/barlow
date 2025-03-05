package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.DetailHeader;
import com.barlow.client.knal.opendata.api.response.common.ItemListBody;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillPreliminaryExaminationInfoResponse(
	@JacksonXmlProperty(localName = "header")
	DetailHeader header,
	@JacksonXmlProperty(localName = "body")
	ItemListBody<BillPreliminaryExaminationInfoItem> body
) {
	public record BillPreliminaryExaminationInfoItem(
		String comitName, // 위원회명
		String submitDt, // 회부일
		String presentDt, // 위원회 상정일
		String procDt, // 위원회 의결일
		String commentName1, // 예비심사 검토 보고서 파일명
		String hwpUrl1, // 예비심사 검토 보고서 HWP 파일 경로
		String pdfUrl1, // 예비심사 검토 보고서 PDF 파일 경로
		String commentName2, // 예비심사 보고서 파일명
		String hwpUrl2, // 예비심사 보고서 HWP 파일 경로
		String pdfUrl2, // 예비심사 보고서 PDF 파일 경로
		String meetingInfoLink // 회의내역 링크 경로
	) {
	}
}
