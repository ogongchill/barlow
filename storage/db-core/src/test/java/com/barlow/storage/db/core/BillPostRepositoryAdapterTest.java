package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.LinkedMultiValueMap;

import com.barlow.core.domain.billpost.BillPostFilterTag;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;
import com.barlow.core.support.SortKey;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

@StorageTest("dummy/billPost.json")
class BillPostRepositoryAdapterTest extends CoreDbContextTest {

	private static final SortKey DEFAULT_SORT_KEY = new SortKey("createdAt#DESC");
	private static final BillPostFilterTag NONE_FILTER = BillPostFilterTag.from(new LinkedMultiValueMap<>());
	private static final BillPostFilterTag LEGISLATION_FILTER = BillPostFilterTag.from(
		new LinkedMultiValueMap<>(Map.of("legislationType", List.of(LegislationType.HOUSE_STEERING.name())))
	);
	private static final BillPostFilterTag PROGRESS_FILTER = BillPostFilterTag.from(
		new LinkedMultiValueMap<>(Map.of("progressStatus", List.of(ProgressStatus.PLENARY_DECIDED.name())))
	);
	private static final BillPostFilterTag PROPOSER_FILTER = BillPostFilterTag.from(
		new LinkedMultiValueMap<>(Map.of("proposerType", List.of(ProposerType.LAWMAKER.name())))
	);
	private static final BillPostFilterTag PARTY_NAME_FILTER = BillPostFilterTag.from(
		new LinkedMultiValueMap<>(Map.of("partyName", List.of(PartyName.PEOPLE_POWER.name())))
	);

	private final BillPostRepositoryAdapter adapter;

	public BillPostRepositoryAdapterTest(BillPostRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("필터링 조건 없이 page,size 만 다르게 하여 법안 게시글을 조회하면 등록되어 있는 모든 법안에서 page,size 에 따라 법안을 반환함")
	@ParameterizedTest
	@MethodSource("provideDiffPageAndSizeBillPostQueryWithExpect")
	void retrieveRecentBillPosts_diffPageAndSize(BillPostQuery query, boolean expectIsLastPage, int expectSize) {
		BillPostsStatus billPostsStatus = adapter.retrieveRecentBillPosts(query);
		assertAll(
			() -> assertThat(billPostsStatus).isNotNull(),
			() -> assertThat(billPostsStatus.isLastPage()).isEqualTo(expectIsLastPage),
			() -> assertThat(billPostsStatus.billPosts()).isNotEmpty().hasSize(expectSize)
		);
	}

	private static Stream<Arguments> provideDiffPageAndSizeBillPostQueryWithExpect() {
		return Stream.of(
			Arguments.of(new BillPostQuery(0, 10, DEFAULT_SORT_KEY, NONE_FILTER), true, 4),
			Arguments.of(new BillPostQuery(0, 1, DEFAULT_SORT_KEY, NONE_FILTER), false, 1),
			Arguments.of(new BillPostQuery(1, 10, DEFAULT_SORT_KEY, NONE_FILTER), true, 0)
		);
	}
}