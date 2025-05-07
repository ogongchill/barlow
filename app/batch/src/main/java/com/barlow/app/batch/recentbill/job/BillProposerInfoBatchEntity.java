package com.barlow.app.batch.recentbill.job;

import java.util.List;
import java.util.Objects;

import com.barlow.client.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.client.knal.opendata.api.response.item.BillPetitionMemberListItem;

public record BillProposerInfoBatchEntity(
	List<BillProposerInfo> billProposerInfos
) {
	public static BillProposerInfoBatchEntity from(ItemResponseBody<BillPetitionMemberListItem> memberList) {
		return new BillProposerInfoBatchEntity(memberList.items()
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
