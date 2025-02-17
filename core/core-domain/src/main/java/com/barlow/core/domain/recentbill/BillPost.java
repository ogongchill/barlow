package com.barlow.core.domain.recentbill;

import java.time.LocalDateTime;
import java.util.List;

public class BillPost {

	private final BillInfo billInfo;
	private final ProposerInfo proposerInfo;
	private final LegislationInfo legislationInfo;
	private final String summary;
	private final String detail;
	private final LocalDateTime createdAt;
	private final int viewCount;
	private List<BillProposer> billProposers;

	public BillPost(
		BillInfo billInfo, ProposerInfo proposerInfo, LegislationInfo legislationInfo,
		String summary, String detail, LocalDateTime createdAt, int viewCount
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

	public String getBillId() {
		return billInfo.billId;
	}

	public String getBillName() {
		return billInfo.billName;
	}

	public String getProposerType() {
		return proposerInfo.proposerType;
	}

	public String getProposers() {
		return proposerInfo.proposers;
	}

	public String getLegislativeBody() {
		return legislationInfo.legislativeBody;
	}

	public String getLegislationProcessStatus() {
		return legislationInfo.legislationProcessStatus;
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
		private final String proposerType;
		private final String proposers;
		public ProposerInfo(String proposerType, String proposers) {
			this.proposerType = proposerType;
			this.proposers = proposers;
		}
	}

	public static class LegislationInfo {
		private final String legislativeBody;
		private final String legislationProcessStatus;
		public LegislationInfo(String legislationType, String legislationProcessStatus) {
			this.legislativeBody = legislationType;
			this.legislationProcessStatus = legislationProcessStatus;
		}
	}
}
