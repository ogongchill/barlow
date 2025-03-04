package com.barlow.core.domain.billpost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public record BillPostQuery(
	@NotNull Integer page,
	@NotNull Integer size,
	@NotNull String sortKey,
	@NotNull Map<String, List<String>> tags
) {
	public static BillPostQuery defaultOf(
		Integer page,
		Integer size,
		String sortKey,
		Map<String, List<String>> tags
	) {
		if (tags == null) {
			tags = new HashMap<>();
		}
		return new BillPostQuery(page, size, sortKey, tags);
	}

	public static BillPostQuery legislationOf(
		String legislationType,
		Integer page,
		Integer size,
		String sortKey,
		Map<String, List<String>> tags
	) {
		if (tags == null) {
			tags = Map.of(
				"legislationType", List.of(legislationType),
				"proposerType", ProposerType.findDefaultTagNames(),
				"progressStatus", ProgressStatus.findDefaultTagNames()
			);
		}
		return new BillPostQuery(page, size, sortKey, tags);
	}
}
