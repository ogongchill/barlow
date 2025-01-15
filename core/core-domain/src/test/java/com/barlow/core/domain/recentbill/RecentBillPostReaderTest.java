package com.barlow.core.domain.recentbill;

import static com.barlow.core.domain.recentbill.RecentBillFixture.BILL_ID_1;
import static com.barlow.core.domain.recentbill.RecentBillFixture.BILL_NAME_1;
import static com.barlow.core.domain.recentbill.RecentBillFixture.DETAIL;
import static com.barlow.core.domain.recentbill.RecentBillFixture.EMPTY_RECENT_BILL_POST_STATUS;
import static com.barlow.core.domain.recentbill.RecentBillFixture.LEGISLATION_PROCESS_STATUS;
import static com.barlow.core.domain.recentbill.RecentBillFixture.LEGISLATION_TYPE;
import static com.barlow.core.domain.recentbill.RecentBillFixture.PROPOSERS;
import static com.barlow.core.domain.recentbill.RecentBillFixture.PROPOSER_TYPE;
import static com.barlow.core.domain.recentbill.RecentBillFixture.RECENT_BILL_POST_1;
import static com.barlow.core.domain.recentbill.RecentBillFixture.SUMMARY;
import static com.barlow.core.domain.recentbill.RecentBillFixture.VIEW_COUNT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.barlow.DevelopTest;

class RecentBillPostReaderTest extends DevelopTest {

	private @Mock RecentBillPostRepository recentBillPostRepository;
	private @InjectMocks RecentBillPostReader recentBillPostReader;

	@DisplayName("페이지, 사이즈, 정렬키, 태그정보를 받아 최근법안게시글 전체를 조회하면 최근법안게시글들의 페이징 결과를 반환한다")
	@Test
	void readRecentBillPosts() {
		BillPostQuery billPostQuery = new BillPostQuery(1, 100, "created_at#asc", Map.of());
		when(recentBillPostRepository.retrieveRecentBillPosts(billPostQuery))
			.thenReturn(EMPTY_RECENT_BILL_POST_STATUS);

		RecentBillPostsStatus result = recentBillPostReader.readRecentBillPosts(billPostQuery);

		assertAll(
			() -> assertThat(result.recentBillPosts()).isEmpty(),
			() -> assertThat(result.isLastPage()).isTrue()
		);
	}

	@DisplayName("법안 ID 를 받아, 최근법안게시글 상세조회하면 최근법안게시글의 상세정보를 반환한다")
	@Test
	void readRecentBillPostDetail() {
		BillPostDetailQuery postDetailQuery = new BillPostDetailQuery(BILL_ID_1);
		when(recentBillPostRepository.retrieveRecentBillPost(postDetailQuery))
			.thenReturn(RECENT_BILL_POST_1);

		RecentBillPost result = recentBillPostReader.readRecentBillPostDetail(postDetailQuery);

		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result.getBillId()).isEqualTo(BILL_ID_1),
			() -> assertThat(result.getBillName()).isEqualTo(BILL_NAME_1),
			() -> assertThat(result.getProposerType()).isEqualTo(PROPOSER_TYPE),
			() -> assertThat(result.getProposers()).isEqualTo(PROPOSERS),
			() -> assertThat(result.getLegislativeBody()).isEqualTo(LEGISLATION_TYPE),
			() -> assertThat(result.getLegislationProcessStatus()).isEqualTo(LEGISLATION_PROCESS_STATUS),
			() -> assertThat(result.getSummary()).isEqualTo(SUMMARY),
			() -> assertThat(result.getDetail()).isEqualTo(DETAIL),
			() -> assertThat(result.getViewCount()).isEqualTo(VIEW_COUNT)
		);
	}

	@DisplayName("법안 ID 를 받아, 최근법안게시글 상세조회 시 값이 없으면 예외를 발생시킨다")
	@Test
	void readRecentBillPostDetailFailure() {
		BillPostDetailQuery postDetailQuery = new BillPostDetailQuery(BILL_ID_1);
		when(recentBillPostRepository.retrieveRecentBillPost(postDetailQuery))
			.thenReturn(null);

		assertThatThrownBy(() -> recentBillPostReader.readRecentBillPostDetail(postDetailQuery))
			.isInstanceOf(RecentBillPostDomainException.class)
			.hasMessage(RecentBillPostDomainException.notFound(BILL_ID_1).getMessage());
	}
}