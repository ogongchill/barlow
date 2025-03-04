package com.barlow.core.support;

public final class SortKey {

	private static final String SPLITTER = "#";
	private static final int SORT_FIELD_INDEX = 0;
	private static final int SORT_ORDER_INDEX = 1;

	private final String value;

	public SortKey(String value) {
		this.value = value;
	}

	public String getSortField() {
		return value.split(SPLITTER)[SORT_FIELD_INDEX];
	}

	public String getSortOrder() {
		return value.split(SPLITTER)[SORT_ORDER_INDEX];
	}
}
