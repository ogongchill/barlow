package com.barlow.batch.core.recentbill;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LawmakerProvider(
	@JsonProperty("lawmakers") List<Lawmaker> lawmakers
) {
	public LawmakerProvider provide(String name, String partyName) {
		return new LawmakerProvider(lawmakers.stream()
			.filter(lawmaker -> lawmaker.name.equals(name) && lawmaker.partyName.equals(partyName))
			.toList()
		);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Lawmaker(
		@JsonProperty("MONA_CD") String code,
		@JsonProperty("HG_NM") String name,
		@JsonProperty("POLY_NM") String partyName,
		@JsonProperty("HOMEPAGE") String profileImagePath // fixme : profile image path 로 변경
	) {
	}
}
