package com.barlow.batch.core.recentbill.client;

import static com.barlow.batch.core.recentbill.job.TodayBillInfoBatchEntity.*;

import java.util.Optional;

import com.barlow.client.knal.opendata.api.response.item.BillInfoListItem;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class TodayBillInfoBatchEntityFactory {

	private TodayBillInfoBatchEntityFactory() {
	}

	public static BillInfoItem make(BillInfoListItem listItem) {
		Optional<ParsedBillName> parsedOpt = BillNameParser.parse(listItem.billName());
		if (parsedOpt.isEmpty()) {
			return null;
		}

		ParsedBillName parsed = parsedOpt.get();

		ProposerType proposerKind = ProposerType.findByValue(listItem.proposerKind());
		String billName = listItem.billName();

		// 정부/의장/기타 유형 처리
		if (isInstitutionalProposal(proposerKind)) {
			return buildFromInstitutionalProposal(listItem, parsed, proposerKind);
		}

		// 대안 법안 + 부가 발의자 존재
		if ("대안".equals(parsed.firstParens()) && parsed.secondParens() != null) {
			return buildFromAlternativeBill(listItem, parsed, proposerKind);
		}

		// 징계안 (의원)
		if (billName.contains("징계안") && parsed.secondParens() != null) {
			return buildFromSanctionProposal(listItem, parsed, proposerKind);
		}

		// 일반 법안 (의원 등)
		if (parsed.secondParens() == null) {
			return buildFromNormalBill(listItem, parsed, proposerKind);
		}

		return null;
	}

	private static boolean isInstitutionalProposal(ProposerType proposerType) {
		return proposerType.isGovernment() || proposerType.isSpeaker() || proposerType.isEtc();
	}

	private static BillInfoItem buildFromInstitutionalProposal(BillInfoListItem item, ParsedBillName parsed,
		ProposerType proposerType) {
		String proposer = parsed.secondParens() == null ? parsed.firstParens() : parsed.secondParens();
		return BillInfoItem.builder()
			.billId(item.billId())
			.billNo(item.billNo())
			.billName(parsed.fullMatch())
			.generalResult(item.generalResult())
			.processingType(item.passGubn())
			.proposerType(proposerType)
			.proposers(proposer)
			.proposeDateStr(item.proposeDt())
			.progressStatusCode(ProgressStatus.findByValue(item.procStageCd()))
			.summary(item.summary())
			.build();
	}

	private static BillInfoItem buildFromAlternativeBill(
		BillInfoListItem item,
		ParsedBillName parsed,
		ProposerType proposerType
	) {
		return BillInfoItem.builder()
			.billId(item.billId())
			.billNo(item.billNo())
			.billName(String.format("%s(%s)", parsed.title(), parsed.firstParens()))
			.generalResult(item.generalResult())
			.processingType(item.passGubn())
			.proposerType(proposerType)
			.proposers(parsed.secondParens())
			.proposeDateStr(item.proposeDt())
			.progressStatusCode(ProgressStatus.findByValue(item.procStageCd()))
			.summary(item.summary())
			.build();
	}

	private static BillInfoItem buildFromSanctionProposal(
		BillInfoListItem item,
		ParsedBillName parsed,
		ProposerType proposerType
	) {
		return BillInfoItem.builder()
			.billId(item.billId())
			.billNo(item.billNo())
			.billName(parsed.fullMatch())
			.generalResult(item.generalResult())
			.processingType(item.passGubn())
			.proposerType(proposerType)
			.proposers(parsed.secondParens())
			.proposeDateStr(item.proposeDt())
			.progressStatusCode(ProgressStatus.findByValue(item.procStageCd()))
			.summary(item.summary())
			.build();
	}

	private static BillInfoItem buildFromNormalBill(
		BillInfoListItem item,
		ParsedBillName parsed,
		ProposerType proposerType
	) {
		return BillInfoItem.builder()
			.billId(item.billId())
			.billNo(item.billNo())
			.billName(parsed.title())
			.generalResult(item.generalResult())
			.processingType(item.passGubn())
			.proposerType(proposerType)
			.proposers(parsed.firstParens())
			.proposeDateStr(item.proposeDt())
			.progressStatusCode(ProgressStatus.findByValue(item.procStageCd()))
			.summary(item.summary())
			.build();
	}
}

