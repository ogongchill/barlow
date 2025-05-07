package com.barlow.app.batch.recentbill.job;

import java.io.Serializable;
import java.util.List;

import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public record TodayBillInfoBatchEntity(
	int totalCount,
	List<BillInfoItem> items
) implements Serializable {

	public int itemSize() {
		return items.size();
	}

	public TodayBillInfoBatchEntity filterReceivedBills() {
		List<BillInfoItem> receivedBills = items.stream()
			.filter(BillInfoItem::isReceipt)
			.toList();
		return new TodayBillInfoBatchEntity(receivedBills.size(), receivedBills);
	}

	public TodayBillInfoBatchEntity filterChairmanBills() {
		List<BillInfoItem> chairmanBills = items.stream()
			.filter(BillInfoItem::isChairman)
			.toList();
		return new TodayBillInfoBatchEntity(chairmanBills.size(), chairmanBills);
	}

	public TodayBillInfoBatchEntity filteredBillsWithFewProposers() {
		List<BillInfoItem> billsWithFewProposers = items.stream()
			.filter(BillInfoItem::isProposerLessThanTwentyOrChairman)
			.toList();
		return new TodayBillInfoBatchEntity(billsWithFewProposers.size(), billsWithFewProposers);
	}

	public record BillInfoItem(
		String billId,
		String billNo,
		String billName,
		String generalResult, // 처리 결과: 폐기,원안가결,수정가결 등등
		String processingType, // 처리구분: 계류의안,처리의안 등
		ProposerType proposerType, // 제안자구분: 의원,위원장,정부,의장 등
		String proposers, // ~ 등 N명
		String proposeDateStr,
		String decisionDateStr,
		ProgressStatus progressStatus, // 심사진행상태: 접수,소관위접수 등
		String summary // 주요내용
	) implements Serializable {
		private static final String NON_DIGIT_REGEX = "\\D";

		boolean isReceipt() {
			return progressStatus.isReceived();
		}

		/**
		 * 위원장으로 발의된 법안은 이미 소관위원회가 정해졌다는 뜻임
		 * 따라서 최초 insert 시에 LegislationType 을 설정할 수 있으므로 해당 메서드 사용함
		 *
		 * @return 위원장 발의 법안
		 */
		boolean isChairman() {
			return proposerType.isChairman();
		}

		/**
		 * 발의자 수가 20명 미만인 법안에 대해서만
		 * 발의자를 insert 하도록 했음
		 *
		 * @return 발의자 수가 20명 미만인 법안
		 */
		boolean isProposerLessThanTwentyOrChairman() {
			String digitStr = proposers.replaceAll(NON_DIGIT_REGEX, "");
			if (digitStr.isBlank()) {
				return true;
			}
			return Integer.parseInt(digitStr) < 20;
		}

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {
			private String billId;
			private String billNo;
			private String billName;
			private String generalResult;
			private String processingType;
			private ProposerType proposerType;
			private String proposers;
			private String proposeDateStr;
			private String decisionDateStr;
			private ProgressStatus progressStatus;
			private String summary;

			public Builder billId(String billId) {
				this.billId = billId;
				return this;
			}

			public Builder billNo(String billNo) {
				this.billNo = billNo;
				return this;
			}

			public Builder billName(String billName) {
				this.billName = billName;
				return this;
			}

			public Builder generalResult(String generalResult) {
				this.generalResult = generalResult;
				return this;
			}

			public Builder processingType(String processingType) {
				this.processingType = processingType;
				return this;
			}

			public Builder proposerType(ProposerType proposerType) {
				this.proposerType = proposerType;
				return this;
			}

			public Builder proposers(String proposers) {
				this.proposers = proposers;
				return this;
			}

			public Builder proposeDateStr(String proposeDateStr) {
				this.proposeDateStr = proposeDateStr;
				return this;
			}

			public Builder decisionDateStr(String decisionDateStr) {
				this.decisionDateStr = decisionDateStr;
				return this;
			}

			public Builder progressStatusCode(ProgressStatus progressStatusCode) {
				this.progressStatus = progressStatusCode;
				return this;
			}

			public Builder summary(String summary) {
				this.summary = summary;
				return this;
			}

			public BillInfoItem build() {
				return new BillInfoItem(
					billId, billNo, billName,
					generalResult, processingType,
					proposerType, proposers, proposeDateStr,
					decisionDateStr, progressStatus, summary
				);
			}
		}
	}
}
