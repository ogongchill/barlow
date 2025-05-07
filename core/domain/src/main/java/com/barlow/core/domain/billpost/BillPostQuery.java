package com.barlow.core.domain.billpost;

import static com.barlow.core.domain.billpost.BillPostFilterTag.LEGISLATION_TYPE_TAG;
import static com.barlow.core.domain.billpost.BillPostFilterTag.PROGRESS_STATUS_TAG;
import static com.barlow.core.domain.billpost.BillPostFilterTag.PROPOSER_TYPE_TAG;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;
import com.barlow.core.support.SortKey;

public record BillPostQuery(
	@NotNull Integer page,
	@NotNull Integer size,
	@NotNull SortKey sortKey,
	@NotNull BillPostFilterTag tags
) {
	public static BillPostQuery defaultOf(
		@NotNull Integer page,
		@NotNull Integer size,
		@NotNull String sortKey,
		@NotNull MultiValueMap<String, String> tags
	) {
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.from(tags));
	}

	public static BillPostQuery legislationOf(
		@NotNull LegislationType legislationType,
		@NotNull Integer page,
		@NotNull Integer size,
		@NotNull String sortKey,
		@NotNull MultiValueMap<String, String> tags
	) {
		if (tags.isEmpty()) {
			tags = new LinkedMultiValueMap<>();
			tags.put(LEGISLATION_TYPE_TAG, List.of(legislationType.name()));
			tags.put(PROPOSER_TYPE_TAG, ProposerType.findDefaultTagNames());
			tags.put(PROGRESS_STATUS_TAG, ProgressStatus.findDefaultTagNames());
		} else {
			tags.set(LEGISLATION_TYPE_TAG, legislationType.name());
		}
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.from(tags));
	}

	public static BillPostQuery preAnnounceOf(
		@NotNull Integer page,
		@NotNull Integer size,
		@NotNull String sortKey,
		@NotNull MultiValueMap<String, String> tags
	) {
		return new BillPostQuery(page, size, new SortKey(sortKey), BillPostFilterTag.preAnnounceFrom(tags));
	}
}
