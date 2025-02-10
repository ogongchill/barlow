package com.barlow.batch.core.recentbill.job;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.barlow.client.knal.api.response.common.ItemListPagingBody;
import com.barlow.client.knal.api.response.item.BillInfoListItem;

public record TodayBillInfoResult(
	int totalCount,
	List<BillInfoItem> items
) implements Serializable {

	public int itemSize() {
		return items.size();
	}

	public static TodayBillInfoResult from(ItemListPagingBody<BillInfoListItem> billInfoItems) {
		return new TodayBillInfoResult(
			billInfoItems.getTotalCount(),
			billInfoItems.getItems().stream()
				.map(BillInfoItem::from)
				.filter(Objects::nonNull)
				.toList()
		);
	}

	public record BillInfoItem(
		String billId,
		String billNo,
		String billName,
		String generalResult, // 처리 결과: 폐기,원안가결,수정가결 등등
		String processingType, // 처리구분: 계류의안,처리의안 등
		String proposerType, // 제안자구분: 의원,위원장,정부,의장 등
		String proposers, // ~ 등 N명
		String proposeDateStr,
		String decisionDateStr,
		String progressStatusCode, // 심사진행상태: 접수,소관위접수 등
		String summary // 주요내용
	) implements Serializable {
		private static final String REGEX_PATTERN_TRAILING_PARENTHESIS = "^(.*?)\\((.*?)\\)(?:\\((.*?)\\))?$";
		private static final String DEFAULT_BILL_PROGRESS_STATUS = "접수";

		static BillInfoItem from(BillInfoListItem listItem) {
			Pattern pattern = Pattern.compile(REGEX_PATTERN_TRAILING_PARENTHESIS);
			Matcher matcher = pattern.matcher(listItem.billName());
			if (matcher.find() && listItem.procStageCd().equals(DEFAULT_BILL_PROGRESS_STATUS)) {
				return new BillInfoItem(
					listItem.billId(),
					listItem.billNo(),
					matcher.group(1),
					listItem.generalResult(),
					listItem.passGubn(),
					listItem.proposerKind(),
					matcher.group(2),
					listItem.proposeDt(),
					listItem.procDt(),
					listItem.procStageCd(),
					listItem.summary()
				);
			} else {
				return null;
			}
		}
	}
}
