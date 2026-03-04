package com.barlow.batch.admin.bill.job;

import java.util.List;
import java.util.Objects;

import com.barlow.infra.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.infra.knal.opendata.api.response.item.BillPetitionMemberListItem;

public record BillProposerInfoResult(
	List<BillProposerInfo> billProposerInfos
) {
	public static BillProposerInfoResult from(ItemResponseBody<BillPetitionMemberListItem> memberList) {
		return new BillProposerInfoResult(memberList.items()
			.stream()
			.filter(Objects::nonNull)
			.map(BillProposerInfo::from)
			.toList()
		);
	}

	public record BillProposerInfo(
		String name,
		String partyName
	) {
		static BillProposerInfo from(BillPetitionMemberListItem item) {
			return new BillProposerInfo(item.memName(), item.polyNm());
		}
	}
}
