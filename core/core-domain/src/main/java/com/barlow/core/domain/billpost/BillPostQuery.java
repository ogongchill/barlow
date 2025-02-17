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
}
