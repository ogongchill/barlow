package com.barlow.batch.core.recentbill;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LawmakerProvider(
	@JsonProperty("lawmakers") List<Lawmaker> lawmakers
) {
	public Lawmaker provide(String name, String partyName) {
		return lawmakers.stream()
			.filter(lawmaker -> lawmaker.matchesNameAndParty(name, partyName))
			.findFirst()
			.orElse(null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Lawmaker(
		@JsonProperty("MONA_CD") String code,
		@JsonProperty("HG_NM") String name,
		@JsonProperty("POLY_NM") String partyName,
		@JsonProperty("PROFILE_IMAGE") String profileImagePath
	) {
		boolean matchesNameAndParty(String name, String partyName) {
			if (partyName == null) {
				return name.equals(this.name);
			}
			return name.equals(this.name) && partyName.equals(this.partyName);
		}
	}
}
