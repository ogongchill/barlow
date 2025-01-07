package com.barlow.core.api.controller.v1.recentbill;

import static com.barlow.core.api.controller.v1.recentbill.RecentBillPostFixture.RECENT_BILL_POSTS_STATUS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecentBillPostsApiSpecComposerTest {

	@DisplayName("오늘 날짜를 받아, 날짜에 따라서 최근법안게시글을 '오늘' 과 '최근' 으로 분류하여 구성한다")
	@ParameterizedTest
	@MethodSource("provideTodayDateAndExpectSize")
	void compose(LocalDate today, int todayExpectSize, int recentExpectSize) {
		RecentBillPostsApiSpecComposer apiSpecComposer = new RecentBillPostsApiSpecComposer(RECENT_BILL_POSTS_STATUS);

		RecentBillPostsResponse result = apiSpecComposer.compose(today);

		assertAll(
			() -> assertThat(result.today()).hasSize(todayExpectSize),
			() -> assertThat(result.recent()).hasSize(recentExpectSize)
		);
	}

	private static Stream<Arguments> provideTodayDateAndExpectSize() {
		return Stream.of(
			Arguments.of(LocalDate.of(2024, 12, 31), 1 ,0),
			Arguments.of(LocalDate.of(2025, 1, 1), 0, 1)
		);
	}
}