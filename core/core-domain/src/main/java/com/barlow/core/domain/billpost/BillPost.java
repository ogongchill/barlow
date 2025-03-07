package com.barlow.core.domain.billpost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class BillPost {

	private final BillInfo billInfo;
	private final ProposerInfo proposerInfo;
	private final LegislationInfo legislationInfo;
	private final String summary;
	private final String detail;
	private final LocalDateTime createdAt;
	private final int viewCount;

	private PreAnnouncementInfo preAnnouncementInfo;
	private List<BillProposer> billProposers;

	public BillPost(
		BillInfo billInfo,
		ProposerInfo proposerInfo,
		LegislationInfo legislationInfo,
		String summary, String detail,
		LocalDateTime createdAt, int viewCount
	) {
		this.billInfo = billInfo;
		this.proposerInfo = proposerInfo;
		this.legislationInfo = legislationInfo;
		this.summary = summary;
		this.detail = detail;
		this.createdAt = createdAt;
		this.viewCount = viewCount;
	}

	public void setBillProposers(List<BillProposer> billProposers) {
		this.billProposers = billProposers;
	}

	public void assignPreAnnouncementInfo(PreAnnouncementInfo preAnnouncementInfo) {
		this.preAnnouncementInfo = preAnnouncementInfo;
	}

	public String getBillId() {
		return billInfo.billId;
	}

	public String getBillName() {
		return billInfo.billName;
	}

	public String getProposerType() {
		return proposerInfo.type.getValue();
	}

	public String getProposers() {
		return proposerInfo.proposers;
	}

	public String getLegislativeBody() {
		return legislationInfo.legislativeBody.getValue();
	}

	public String getLegislationProcessStatus() {
		return legislationInfo.legislationProcessStatus.getValue();
	}

	public String getSummary() {
		return summary;
	}

	public String getDetail() {
		return detail;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getViewCount() {
		return viewCount;
	}

	public int calculateDeadlineDay(LocalDate now) {
		return (int)ChronoUnit.DAYS.between(now, preAnnouncementInfo.deadline);
	}

	public String getPreAnnouncementUrl() {
		return preAnnouncementInfo.linkUrl;
	}

	public LocalDate getPreAnnounceDeadline() {
		return preAnnouncementInfo.deadline;
	}

	public List<BillProposer> getBillProposers() {
		return billProposers;
	}

	public static class BillInfo {
		private final String billId;
		private final String billName;

		public BillInfo(String billId, String billName) {
			this.billId = billId;
			this.billName = billName;
		}
	}

	public static class ProposerInfo {
		private final ProposerType type;
		private final String proposers;

		public ProposerInfo(ProposerType type, String proposers) {
			this.type = type;
			this.proposers = proposers;
		}
	}

	public static class LegislationInfo {
		private final LegislationType legislativeBody;
		private final ProgressStatus legislationProcessStatus;

		public LegislationInfo(LegislationType legislationType, ProgressStatus legislationProcessStatus) {
			this.legislativeBody = legislationType;
			this.legislationProcessStatus = legislationProcessStatus;
		}
	}

	public static class PreAnnouncementInfo {
		private final String linkUrl;
		private final LocalDate deadline;

		public PreAnnouncementInfo(String linkUrl, LocalDate deadline) {
			this.linkUrl = linkUrl;
			this.deadline = deadline;
		}
	}
}
