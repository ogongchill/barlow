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
	private final PreAnnouncementInfo preAnnouncementInfo;
	private final List<BillProposer> billProposers;

	public BillPost(BillInfo billInfo, ProposerInfo proposerInfo, LegislationInfo legislationInfo, String summary,
		String detail, LocalDateTime createdAt, int viewCount, List<BillProposer> billProposers,
		PreAnnouncementInfo preAnnouncementInfo) {
		this.billInfo = billInfo;
		this.proposerInfo = proposerInfo;
		this.legislationInfo = legislationInfo;
		this.summary = summary;
		this.detail = detail;
		this.createdAt = createdAt;
		this.viewCount = viewCount;
		this.billProposers = billProposers != null ? List.copyOf(billProposers) : List.of();
		this.preAnnouncementInfo = preAnnouncementInfo;
	}

	public String getBillId() {
		return billInfo.billId();
	}

	public String getBillName() {
		return billInfo.billName();
	}

	public String getProposerType() {
		return proposerInfo.type().getValue();
	}

	public String getProposers() {
		return proposerInfo.proposers();
	}

	public String getLegislativeBody() {
		return legislationInfo.legislativeBody().getValue();
	}

	public String getLegislationProcessStatus() {
		return legislationInfo.legislationProcessStatus().getValue();
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
		return (int)ChronoUnit.DAYS.between(now, preAnnouncementInfo.deadline());
	}

	public String getPreAnnouncementUrl() {
		return preAnnouncementInfo.linkUrl();
	}

	public LocalDate getPreAnnounceDeadline() {
		return preAnnouncementInfo.deadline();
	}

	public List<BillProposer> getBillProposers() {
		return billProposers;
	}

	public record BillInfo(String billId, String billName) {
	}

	public record ProposerInfo(ProposerType type, String proposers) {
	}

	public record LegislationInfo(LegislationType legislativeBody, ProgressStatus legislationProcessStatus) {
	}

	public record PreAnnouncementInfo(String linkUrl, LocalDate deadline) {
	}
}
