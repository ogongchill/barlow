package com.barlow.client.knal.api.response;

import java.util.List;

import com.barlow.client.knal.api.response.item.ComitExaminationItem;
import com.barlow.client.knal.api.response.item.CommitMeetingItem;
import com.barlow.client.knal.api.response.item.JurisdictionExaminationItem;
import com.barlow.client.knal.api.response.item.JurisdictionMeetingItem;
import com.barlow.client.knal.api.response.item.ProcExaminationItem;
import com.barlow.client.knal.api.response.item.ProcMeetingItem;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
