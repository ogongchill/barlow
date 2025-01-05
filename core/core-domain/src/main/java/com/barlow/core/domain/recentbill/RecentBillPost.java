package com.barlow.core.domain.recentbill;

import java.time.LocalDateTime;

public class RecentBillPost {

	private final BillInfo billInfo;
	private final ProposerInfo proposerInfo;
	private final LegislationInfo legislationInfo;
	private final String summary;
	private final String detail;
	private final LocalDateTime createdAt;
	private final int viewCount;

	public RecentBillPost(
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

	public String getBillId() {
		return billInfo.billId;
	}

	public String getBillName() {
		return billInfo.billName;
	}

	public String getProposers() {
		return proposerInfo.proposers;
	}

	public String getLegislationProcessStatus() {
		return legislationInfo.legislationProcessStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getViewCount() {
		return viewCount;
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
		private final String legislationType;
		private final String legislationProcessStatus;
		public LegislationInfo(String legislationType, String legislationProcessStatus) {
			this.legislationType = legislationType;
			this.legislationProcessStatus = legislationProcessStatus;
		}
	}
}
