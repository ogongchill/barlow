package com.barlow.core.api.controller.v1.preannounce;

import org.springframework.util.MultiValueMap;

import com.barlow.core.support.request.PagingSortFilterRequest;

public final class PreAnnounceBillPostsRequest extends PagingSortFilterRequest {

	private static final String SORT_DEADLINE_ASC = "SORT_DEADLINE_ASC"; // 마감순
	private static final String SORT_DEADLINE_DESC = "SORT_DEADLINE_DESC"; // 마감 늦는 순

	private PreAnnounceBillPostsRequest(MultiValueMap<String, String> parameters) {
		super(parameters);
	}

	static PreAnnounceBillPostsRequest sanitizeFrom(MultiValueMap<String, String> parameters) {
		if (!parameters.containsKey(PAGE_KEY)) {
			parameters.set(PAGE_KEY, "0");
		}
		if (!parameters.containsKey(SIZE_KEY)) {
			parameters.set(SIZE_KEY, "10");
		}
		if (!parameters.containsKey(SORT_KEY)) {
			parameters.set(SORT_KEY, "preAnnouncementInfo.deadlineDate#ASC");
		} else {
			String sort = parameters.get(SORT_KEY).getFirst();
			if (sort.equals(SORT_DEADLINE_ASC)) {
				parameters.set(SORT_KEY, "preAnnouncementInfo.deadlineDate#ASC");
			} else if (sort.equals(SORT_DEADLINE_DESC)) {
				parameters.set(SORT_KEY, "preAnnouncementInfo.deadlineDate#DESC");
			}
		}
		return new PreAnnounceBillPostsRequest(parameters);
	}
}
