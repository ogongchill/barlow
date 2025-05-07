package com.barlow.app.support.request;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class PagingSortFilterRequest {

	protected static final String PAGE_KEY = "page";
	protected static final String SIZE_KEY = "size";
	protected static final String SORT_KEY = "sort";

	protected final MultiValueMap<String, String> parameters;

	protected PagingSortFilterRequest(MultiValueMap<String, String> parameters) {
		this.parameters = parameters;
	}

	public int getPage() {
		return Integer.parseInt(Objects.requireNonNull(parameters.getFirst(PAGE_KEY)));
	}

	public int getSize() {
		return Integer.parseInt(Objects.requireNonNull(parameters.getFirst(SIZE_KEY)));
	}

	public String getSort() {
		return Objects.requireNonNull(parameters.getFirst(SORT_KEY));
	}

	public MultiValueMap<String, String> getFilters() {
		return parameters.entrySet().stream()
			.filter(entry -> !entry.getKey().equals(PAGE_KEY)
				&& !entry.getKey().equals(SIZE_KEY)
				&& !entry.getKey().equals(SORT_KEY)
			)
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue,
				(existing, replacement) -> existing, // 중복 키가 있을 경우 기존 값 유지
				LinkedMultiValueMap::new
			));
	}
}
