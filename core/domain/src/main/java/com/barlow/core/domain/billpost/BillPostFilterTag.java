package com.barlow.core.domain.billpost;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.MultiValueMap;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public final class BillPostFilterTag {

	static final String LEGISLATION_TYPE_TAG = "legislationType";
	static final String PROGRESS_STATUS_TAG = "progressStatus";
	static final String PROPOSER_TYPE_TAG = "proposerType";
	static final String PARTY_NAME_TAG = "partyName";

	private final Set<LegislationType> legislationTypeTags;
	private final Set<ProgressStatus> progressStatusTags;
	private final Set<ProposerType> proposerTypeTags;
	private final Set<PartyName> partyNameTags;
	private final boolean isPreAnnouncement;

	private BillPostFilterTag(
		Set<LegislationType> legislationTypeTags,
		Set<ProgressStatus> progressStatusTags,
		Set<ProposerType> proposerTypeTags,
		Set<PartyName> partyNameTags,
		boolean isPreAnnouncement
	) {
		this.legislationTypeTags = legislationTypeTags;
		this.progressStatusTags = progressStatusTags;
		this.proposerTypeTags = proposerTypeTags;
		this.partyNameTags = partyNameTags;
		this.isPreAnnouncement = isPreAnnouncement;
	}

	public static BillPostFilterTag from(MultiValueMap<String, String> tags) {
		return createFilterTag(tags, false);
	}

	public static BillPostFilterTag preAnnounceFrom(MultiValueMap<String, String> tags) {
		return createFilterTag(tags, true);
	}

	@SuppressWarnings("unchecked")
	private static BillPostFilterTag createFilterTag(MultiValueMap<String, String> tags, boolean isPreAnnouncement) {
		if (tags.isEmpty()) {
			return new BillPostFilterTag(Set.of(), Set.of(), Set.of(), Set.of(), isPreAnnouncement);
		}
		Map<String, Function<String, Enum<?>>> tagMappers = Map.of(
			LEGISLATION_TYPE_TAG, s -> LegislationType.valueOf(s.toUpperCase()),
			PROGRESS_STATUS_TAG, s -> ProgressStatus.valueOf(s.toUpperCase()),
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
			(Set<LegislationType>)results.getOrDefault(LEGISLATION_TYPE_TAG, Set.of()),
			(Set<ProgressStatus>)results.getOrDefault(PROGRESS_STATUS_TAG, Set.of()),
			(Set<ProposerType>)results.getOrDefault(PROPOSER_TYPE_TAG, Set.of()),
			(Set<PartyName>)results.getOrDefault(PARTY_NAME_TAG, Set.of()),
			isPreAnnouncement
		);
	}

	public boolean isPreAnnouncement() {
		return isPreAnnouncement;
	}

	public Set<LegislationType> getLegislationTypeTags() {
		return legislationTypeTags;
	}

	public Set<ProgressStatus> getProgressStatusTags() {
		return progressStatusTags;
	}

	public Set<ProposerType> getProposerTypeTags() {
		return proposerTypeTags;
	}

	public Set<PartyName> getPartyNameTags() {
		return partyNameTags;
	}
}
