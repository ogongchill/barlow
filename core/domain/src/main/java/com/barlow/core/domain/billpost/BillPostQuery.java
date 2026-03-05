package com.barlow.core.domain.billpost;

import static com.barlow.core.domain.billpost.BillPostFilterTag.LEGISLATION_TYPE_TAG;
import static com.barlow.core.domain.billpost.BillPostFilterTag.PROGRESS_STATUS_TAG;
import static com.barlow.core.domain.billpost.BillPostFilterTag.PROPOSER_TYPE_TAG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;
import com.barlow.core.support.SortKey;

public record BillPostQuery(
	Integer page,
	Integer size,
	SortKey sortKey,
	BillPostFilterTag tags) {
	public static BillPostQuery defaultOf(Integer page, Integer size, String sortKey,
		Map<String, List<String>> tags) {
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.from(tags));
	}

	public static BillPostQuery legislationOf(LegislationType legislationType, Integer page,
		Integer size, String sortKey, Map<String, List<String>> tags) {
		if (tags.isEmpty()) {
			tags = new HashMap<>();
			tags.put(LEGISLATION_TYPE_TAG, List.of(legislationType.name()));
			tags.put(PROPOSER_TYPE_TAG, ProposerType.findDefaultTagNames());
			tags.put(PROGRESS_STATUS_TAG, ProgressStatus.findDefaultTagNames());
		} else {
			tags.put(LEGISLATION_TYPE_TAG, new ArrayList<>(List.of(legislationType.name())));
		}
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.from(tags));
	}

	public static BillPostQuery preAnnounceOf(Integer page, Integer size, String sortKey,
		Map<String, List<String>> tags) {
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.preAnnounceFrom(tags));
	}
}
