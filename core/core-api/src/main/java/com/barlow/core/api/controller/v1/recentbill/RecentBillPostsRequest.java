package com.barlow.core.api.controller.v1.recentbill;

import org.springframework.util.MultiValueMap;

import com.barlow.core.support.request.PagingSortFilterRequest;

public final class RecentBillPostsRequest extends PagingSortFilterRequest {

	private static final String SORT_CREATED_AT_DESC = "SORT_CREATED_AT_DESC";
	private static final String SORT_CREATED_AT_ASC = "SORT_CREATED_AT_ASC";

	private RecentBillPostsRequest(MultiValueMap<String, String> parameters) {
		super(parameters);
	}

	static RecentBillPostsRequest sanitizeFrom(MultiValueMap<String, String> parameters) {
		if (!parameters.containsKey(PAGE_KEY)) {
			parameters.set(PAGE_KEY, "0");
		}
		if (!parameters.containsKey(SIZE_KEY)) {
			parameters.set(SIZE_KEY, "10");
		}
		if (!parameters.containsKey(SORT_KEY)) {
			parameters.set(SORT_KEY, "createdAt#DESC");
		} else {
			String sort = parameters.get(SORT_KEY).getFirst();
			if (sort.equals(SORT_CREATED_AT_ASC)) {
				parameters.set(SORT_KEY, "createdAt#ASC");
			} else if (sort.equals(SORT_CREATED_AT_DESC)) {
				parameters.set(SORT_KEY, "createdAt#DESC");
			}
		}
		return new RecentBillPostsRequest(parameters);
	}
}
