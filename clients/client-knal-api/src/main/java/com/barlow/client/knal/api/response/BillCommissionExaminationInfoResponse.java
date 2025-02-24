package com.barlow.client.knal.api.response;

import java.util.List;

import com.barlow.client.knal.api.response.common.DetailHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record BillCommissionExaminationInfoResponse(
	DetailHeader header,
	BillCommissionExaminationInfoBody body
) {
	public record BillCommissionExaminationInfoBody(
		@JacksonXmlProperty(localName = "JurisdictionExamination")
		List<JurisdictionExaminationItem> jurisdictionExamination,// 소관위 심사 정보
		@JacksonXmlProperty(localName = "JurisdictionMeeting")
		List<JurisdictionMeetingItem> jurisdictionMeeting,// 소관위 회의 정보
		@JacksonXmlProperty(localName = "procExamination")
		List<ProcExaminationItem> procExamination, // 법사위 체계 자구심사 정보
		@JacksonXmlProperty(localName = "procMeeting")
		List<ProcMeetingItem> procMeeting, // 법사위 회의 정보
		@JacksonXmlProperty(localName = "comitExamination")
		List<ComitExaminationItem> comitExamination, // 관련위 심사 정보
		@JacksonXmlProperty(localName = "commitMeeting")
		List<CommitMeetingItem> commitMeeting // 관련위 회의 정보
	) {
	}

	public record JurisdictionExaminationItem(
		String committeeName, //소관위원회
		String submitDt, // 회부일
		String presentDt, // 상정일
		String procDt, // 처리일
		String procResultCd, //처리 결과
		String docName1, // 문서1 파일명
		String docName2, // 문서2 파일명
		String hwpUrl1, // 문서1 HWP파일 경로
		String hwpUrl2, // 문서2 HWP파일 경로
		String pdfUrl1, // 문서1 PDF파일 경로
		String pdfUrl2// 문서2 PDF파일 경로
	) {
	}

	public record JurisdictionMeetingItem(
		String confName, // 회의명
		String confDt, // 회의일
		String confResult, //회의 결과
		String pdfUrl, // 회의록 PDF파일 경로
		String fileUrl // 문서에는 나와있지만 응답에는 있는 항목
	) {
	}

	public record ProcExaminationItem(
		String submitDt, // 회부일
		String presentDt, // 상정일
		String procDt, // 처리일
		String procResultCd, // 처리결과
		String hwpUrl, // 체계자구검토보고서 hwp 파일 명
		String pdfUrl // 체계자구검토보고서 pdf 파일명
	) {
	}

	public record ProcMeetingItem(
		String confName, // 회의명
		String confDt, // 회의일
		String confResult, // 회의결과
		String pdfUrl, // 회의록 PDF 파일 경로
		String fileUrl // 문서에는 없지만 실제로 응답되는 항목
	) {
	}

	public record ComitExaminationItem(
		String comitName, // 관련위원회
		String submitDt, // 회부일
		String presentDt, // 상정일
		String procDt, // 의견서제시일
		String pdfUrl // 문서
	) {
	}


	public record CommitMeetingItem(
		String confName, // 회의명
		String confDt, // 회의일
		String confResult, // 회의 결과
		String fileName // 회의록
	) {
	}
}