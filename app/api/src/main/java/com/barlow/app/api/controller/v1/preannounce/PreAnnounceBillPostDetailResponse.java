package com.barlow.app.api.controller.v1.preannounce;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.barlow.app.support.response.Constant;

public record PreAnnounceBillPostDetailResponse(
	String title,
	String proposerSummary,
	String legislativeBody,
	String detail,
	PreAnnouncementSection preAnnouncementSection,
	SummarySection summarySection,
	ProposerSection proposerSection
) {

	record PreAnnouncementSection(
		LocalDate deadline,
		String linkUrl,
		int dDay
	) {
	}

	record SummarySection(
		String summaryTitle,
		String summaryDetail
	) {
		private static final String SUMMARY_TITLE = "AI가 요약했어요";

		SummarySection(String summaryDetail) {
			this(SUMMARY_TITLE, summaryDetail);
		}
	}

	record ProposerSection(
		Map<String, Integer> proposerPartyRate,
		List<ProposerResponse> proposerResponses
	) {
	}

	record ProposerResponse(
		String code,
		String name,
		String profileImage,
		String partyName
	) {
		ProposerResponse(String code, String name, String profileImage, String partyName) {
			this.code = code;
			this.name = name;
			this.profileImage = Constant.IMAGE_ACCESS_URL + profileImage;
			this.partyName = partyName;
		}
	}
}
