package com.barlow.storage.db.core;

import static com.barlow.core.enumerate.LegislationType.EMPTY;
import static com.barlow.core.enumerate.LegislationType.HOUSE_STEERING;
import static com.barlow.core.enumerate.LegislationType.LEGISLATION_AND_JUDICIARY;
import static com.barlow.core.enumerate.ProgressStatus.COMMITTEE_RECEIVED;
import static com.barlow.core.enumerate.ProgressStatus.COMMITTEE_REVIEW;
import static com.barlow.core.enumerate.ProgressStatus.PLENARY_DECIDED;
import static com.barlow.core.enumerate.ProgressStatus.RECEIVED;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.jpa.domain.Specification;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

@StorageTest("dummy/billPost.json")
class BillPostSpecificationsTest extends CoreDbContextTest {

	private final BillPostJpaRepository billPostJpaRepository;

	public BillPostSpecificationsTest(BillPostJpaRepository billPostJpaRepository) {
		this.billPostJpaRepository = billPostJpaRepository;
	}

	@DisplayName("입법예고 여부가 true 이면 preAnnounceInfo IS NOT NULL 이 추가되고, false 이면 조건이 무시된다")
	@ParameterizedTest
	@CsvSource(value = {"true:2", "false:4"}, delimiter = ':')
	void spec_isPreAnnouncement(boolean isPreAnnounceMode, int expectSize) {
		Specification<BillPostJpaEntity> spec = BillPostSpecifications.isPreAnnouncement(isPreAnnounceMode);

		List<BillPostJpaEntity> actual = billPostJpaRepository.findAll(spec);

		assertThat(actual).isNotEmpty().hasSize(expectSize);
	}

	@DisplayName("상임위원회 tag 가 있으면 IN 절이 추가되고, tag 가 없으면 조건이 무시된다")
	@ParameterizedTest
	@MethodSource("provideLegislationTypeTagsAndExpectSize")
	void spec_hasLegislationTypeTag(Set<LegislationType> tags, int expectSize) {
		Specification<BillPostJpaEntity> spec = BillPostSpecifications.hasLegislationTypeTag(tags);

		List<BillPostJpaEntity> actual = billPostJpaRepository.findAll(spec);

		assertThat(actual).isNotEmpty().hasSize(expectSize);
	}

	private static Stream<Arguments> provideLegislationTypeTagsAndExpectSize() {
		return Stream.of(
			Arguments.of(Set.of(), 4),
			Arguments.of(Set.of(EMPTY), 1),
			Arguments.of(Set.of(HOUSE_STEERING), 2),
			Arguments.of(Set.of(LEGISLATION_AND_JUDICIARY), 1),
			Arguments.of(Set.of(HOUSE_STEERING, LEGISLATION_AND_JUDICIARY), 3),
			Arguments.of(Set.of(HOUSE_STEERING, LEGISLATION_AND_JUDICIARY, EMPTY), 4)
		);
	}

	@DisplayName("입법프로세스 상태 tag 가 있으면 IN 절이 추가되고, tag 가 없으면 조건이 무시된다")
	@ParameterizedTest
	@MethodSource("provideProgressStatusTagsAndExpectSize")
	void spec_hasProgressStatusTag(Set<ProgressStatus> tags, int expectSize) {
		Specification<BillPostJpaEntity> spec = BillPostSpecifications.hasProgressStatusTag(tags);

		List<BillPostJpaEntity> actual = billPostJpaRepository.findAll(spec);

		assertThat(actual).isNotEmpty().hasSize(expectSize);
	}

	private static Stream<Arguments> provideProgressStatusTagsAndExpectSize() {
		return Stream.of(
			Arguments.of(Set.of(), 4),
			Arguments.of(Set.of(RECEIVED), 1),
			Arguments.of(Set.of(COMMITTEE_RECEIVED), 1),
			Arguments.of(Set.of(COMMITTEE_REVIEW), 1),
			Arguments.of(Set.of(PLENARY_DECIDED), 1),
			Arguments.of(Set.of(RECEIVED, COMMITTEE_RECEIVED), 2),
			Arguments.of(Set.of(RECEIVED, COMMITTEE_RECEIVED, COMMITTEE_REVIEW), 3),
			Arguments.of(Set.of(RECEIVED, COMMITTEE_RECEIVED, COMMITTEE_REVIEW, PLENARY_DECIDED), 4)
		);
	}

	@DisplayName("발의자 tag 가 있으면 IN 절이 추가되고, tag 가 없으면 조건이 무시된다")
	@ParameterizedTest
	@MethodSource("provideProposerTypeTagsAndExpectSize")
	void spec_hasProposerTypeTag(Set<ProposerType> tags, int expectSize) {
		Specification<BillPostJpaEntity> spec = BillPostSpecifications.hasProposerTypeTag(tags);

		List<BillPostJpaEntity> actual = billPostJpaRepository.findAll(spec);

		assertThat(actual).isNotEmpty().hasSize(expectSize);
	}

	private static Stream<Arguments> provideProposerTypeTagsAndExpectSize() {
		return Stream.of(
			Arguments.of(Set.of(), 4),
			Arguments.of(Set.of(ProposerType.LAWMAKER), 3),
			Arguments.of(Set.of(ProposerType.CHAIRMAN), 1),
			Arguments.of(Set.of(ProposerType.LAWMAKER, ProposerType.CHAIRMAN), 4)
		);
	}

	@DisplayName("당명 tag 가 있으면 IN 절이 추가되고, tag 가 없으면 조건이 무시된다")
	@ParameterizedTest
	@MethodSource("providePartyNameTagsAndExpectSize")
	void spec_hasPartyNameTag(Set<PartyName> tags, int expectSize) {
		Specification<BillPostJpaEntity> spec = BillPostSpecifications.hasPartyNameTag(tags);

		List<BillPostJpaEntity> actual = billPostJpaRepository.findAll(spec);

		assertThat(actual).isNotEmpty().hasSize(expectSize);
	}

	private static Stream<Arguments> providePartyNameTagsAndExpectSize() {
		return Stream.of(
			Arguments.of(Set.of(), 4),
			Arguments.of(Set.of(PartyName.PEOPLE_POWER), 3),
			Arguments.of(Set.of(PartyName.MINJOO), 3),
			Arguments.of(Set.of(PartyName.INDEPENDENT), 3),
			Arguments.of(Set.of(PartyName.PEOPLE_POWER, PartyName.MINJOO), 3),
			Arguments.of(Set.of(PartyName.PEOPLE_POWER, PartyName.MINJOO, PartyName.INDEPENDENT), 3)
		);
	}
}