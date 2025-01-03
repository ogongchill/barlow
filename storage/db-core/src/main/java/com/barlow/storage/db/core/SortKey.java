package com.barlow.storage.db.core;

import org.springframework.data.domain.Sort;

public final class SortKey {

	private static final String SPLITTER = "#";
	private static final int SORT_FIELD_INDEX = 0;
	private static final int SORT_ORDER_INDEX = 1;

	private final String value;

	public SortKey(String value) {
		this.value = value;
	}

	Sort getSort() {
		return Sort.by(Sort.Direction.valueOf(getSortOrder()), getSortField());
	}

	private String getSortField() {
		return value.split(SPLITTER)[SORT_FIELD_INDEX];
	}

	private String getSortOrder() {
		return value.split(SPLITTER)[SORT_ORDER_INDEX];
	}
}
