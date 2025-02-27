package com.barlow.core.domain.billpost;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.barlow.core.enumerate.ProgressStatus;

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
				ProgressStatus.COMMITTEE_RECEIVED.name(),
				ProgressStatus.COMMITTEE_REVIEW.name(),
				ProgressStatus.PLENARY_SUBMITTED.name(),
				ProgressStatus.PLENARY_DECIDED.name(),
				ProgressStatus.WITHDRAWN.name(),
				ProgressStatus.GOVERNMENT_TRANSFERRED.name(),
				ProgressStatus.REDEMAND_REQUESTED.name(),
				ProgressStatus.REJECTED.name(),
				ProgressStatus.PROMULGATED.name()
			)
		));
		return new BillPostQuery(page, size, sortKey, tags);
	}
}
