package com.barlow.core.domain.billpost;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public record BillPostQuery(
	@NotNull Integer page,
	@NotNull Integer size,
	@NotNull String sortKey,
	@NotNull Map<String, List<String>> tags
) {
	public static BillPostQuery legislationOf(
		String legislationType,
		Integer page,
		Integer size,
		String sortKey,
		Map<String, List<String>> tags
	) {
		String legislationTypeKey = "legislationType";
		String progressStatusKey = "progressStatus";
		tags.putAll(Map.of(
			legislationTypeKey, List.of(legislationType),
			progressStatusKey, List.of(
				"COMMITTEE_RECEIVED",
				"COMMITTEE_REVIEW",
				"PLENARY_SUBMITTED",
				"PLENARY_DECIDED",
				"WITHDRAWN",
				"GOVERNMENT_TRANSFERRED",
				"REDEMAND_REQUESTED",
				"REJECTED",
				"PROMULGATED"
			)
		));
		return new BillPostQuery(page, size, sortKey, tags);
	}
}
