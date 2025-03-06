package com.barlow.client.knal.opencongress.api.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Head(
	@JsonProperty("list_total_count")
	Integer listTotalCount,
	@JsonProperty("RESULT")
	Result result
) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Result(
		@JsonProperty("CODE")
		String code,
		@JsonProperty("MESSAGE")
		String message
	) {
	}
}
