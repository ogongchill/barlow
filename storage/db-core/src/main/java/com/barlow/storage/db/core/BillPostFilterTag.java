package com.barlow.storage.db.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BillPostFilterTag {

	private static final String LEGISLATION_TYPE_TAG = "legislationType";
	private static final String LEGISLATION_STATUS_TAG = "legislationStatus";
	private static final String PROPOSER_TYPE_TAG = "proposerType";
	private static final String PARTY_NAME_TAG = "partyName";

	private final Set<LegislationType> legislationTypeTags;
	private final Set<LegislationStatus> legislationStatusTags;
	private final Set<ProposerType> proposerTypeTags;
	private final Set<PartyName> partyNameTags;

	public BillPostFilterTag(
		Set<LegislationType> legislationTypeTags,
		Set<LegislationStatus> legislationStatusTags,
		Set<ProposerType> proposerTypeTags,
		Set<PartyName> partyNameTags
	) {
		this.legislationTypeTags = legislationTypeTags;
		this.legislationStatusTags = legislationStatusTags;
		this.proposerTypeTags = proposerTypeTags;
		this.partyNameTags = partyNameTags;
	}

	@SuppressWarnings("unchecked")
	static BillPostFilterTag from(Map<String, List<String>> tags) {
		if (tags.isEmpty()) {
			return new BillPostFilterTag(Set.of(), Set.of(), Set.of(), Set.of());
		}
		Map<String, Function<String, Enum<?>>> tagMappers = Map.of(
			LEGISLATION_TYPE_TAG, s -> LegislationType.valueOf(s.toUpperCase()),
			LEGISLATION_STATUS_TAG, s -> LegislationStatus.valueOf(s.toUpperCase()),
			PROPOSER_TYPE_TAG, s -> ProposerType.valueOf(s.toUpperCase()),
			PARTY_NAME_TAG, s -> PartyName.valueOf(s.toUpperCase())
		);

		Map<String, Set<? extends Enum<?>>> results = tags.entrySet()
			.stream()
			.filter(entry -> tagMappers.containsKey(entry.getKey())) // 지원되는 키만 필터링
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> entry.getValue().stream()
					.map(tagMappers.get(entry.getKey()))
					.collect(Collectors.toSet())
			));

		return new BillPostFilterTag(
			(Set<LegislationType>) results.getOrDefault(LEGISLATION_TYPE_TAG, Set.of()),
			(Set<LegislationStatus>) results.getOrDefault(LEGISLATION_STATUS_TAG, Set.of()),
			(Set<ProposerType>) results.getOrDefault(PROPOSER_TYPE_TAG, Set.of()),
			(Set<PartyName>) results.getOrDefault(PARTY_NAME_TAG, Set.of())
		);
	}

	boolean isPartyNameTagEmpty() {
		return partyNameTags.isEmpty();
	}

	Set<LegislationType> getLegislationTypeTags() {
		return legislationTypeTags;
	}

	Set<LegislationStatus> getLegislationStatusTags() {
		return legislationStatusTags;
	}

	Set<ProposerType> getProposerTypeTags() {
		return proposerTypeTags;
	}

	Set<PartyName> getPartyNameTags() {
		return partyNameTags;
	}
}
