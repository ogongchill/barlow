package com.barlow.batch.core.recentbill.client;

import java.util.List;

import com.barlow.client.knal.api.response.common.ItemListBody;
import com.barlow.client.knal.api.response.item.BillPetitionMemberListItem;

public record BillProposerInfoResult(
	List<BillProposerInfo> billProposerInfos
) {
	static BillProposerInfoResult from(ItemListBody<BillPetitionMemberListItem> memberList) {
		return new BillProposerInfoResult(memberList.getItems()
			.stream()
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
