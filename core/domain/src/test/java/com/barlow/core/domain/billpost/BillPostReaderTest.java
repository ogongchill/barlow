package com.barlow.core.domain.billpost;

import static com.barlow.core.domain.billpost.BillPostFixture.BILL_ID_1;
import static com.barlow.core.domain.billpost.BillPostFixture.BILL_NAME_1;
import static com.barlow.core.domain.billpost.BillPostFixture.DETAIL;
import static com.barlow.core.domain.billpost.BillPostFixture.EMPTY_RECENT_BILL_POST_STATUS;
import static com.barlow.core.domain.billpost.BillPostFixture.LEGISLATION_PROCESS_STATUS;
import static com.barlow.core.domain.billpost.BillPostFixture.LEGISLATION_TYPE;
import static com.barlow.core.domain.billpost.BillPostFixture.PROPOSERS;
import static com.barlow.core.domain.billpost.BillPostFixture.PROPOSER_TYPE;
import static com.barlow.core.domain.billpost.BillPostFixture.RECENT_BILL_POST_1;
import static com.barlow.core.domain.billpost.BillPostFixture.SUMMARY;
import static com.barlow.core.domain.billpost.BillPostFixture.VIEW_COUNT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.util.LinkedMultiValueMap;

import com.barlow.DevelopTest;
import com.barlow.core.support.SortKey;

class BillPostReaderTest extends DevelopTest {

	private @Mock BillPostRepository billPostRepository;
	private @InjectMocks BillPostReader billPostReader;

	@DisplayName("페이지, 사이즈, 정렬키, 태그정보를 받아 최근법안게시글 전체를 조회하면 최근법안게시글들의 페이징 결과를 반환한다")
	@Test
	void readBillPosts() {
		BillPostQuery billPostQuery = new BillPostQuery(
			1, 100, new SortKey("createdAt#DESC"), BillPostFilterTag.from(new LinkedMultiValueMap<>()
		));
		when(billPostRepository.retrieveRecentBillPosts(billPostQuery))
			.thenReturn(EMPTY_RECENT_BILL_POST_STATUS);

		BillPostsStatus result = billPostReader.readBillPosts(billPostQuery);

		assertAll(
			() -> assertThat(result.billPosts()).isEmpty(),
			() -> assertThat(result.isLastPage()).isTrue()
		);
	}

	@DisplayName("법안 ID 를 받아, 최근법안게시글 상세조회하면 최근법안게시글의 상세정보를 반환한다")
	@Test
	void readBillPostDetail() {
		BillPostDetailQuery postDetailQuery = new BillPostDetailQuery(BILL_ID_1);
		when(billPostRepository.retrieveRecentBillPost(postDetailQuery))
			.thenReturn(RECENT_BILL_POST_1);

		BillPost result = billPostReader.readBillPostDetail(postDetailQuery);

		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result.getBillId()).isEqualTo(BILL_ID_1),
			() -> assertThat(result.getBillName()).isEqualTo(BILL_NAME_1),
			() -> assertThat(result.getProposerType()).isEqualTo(PROPOSER_TYPE.getValue()),
			() -> assertThat(result.getProposers()).isEqualTo(PROPOSERS),
			() -> assertThat(result.getLegislativeBody()).isEqualTo(LEGISLATION_TYPE.getValue()),
			() -> assertThat(result.getLegislationProcessStatus()).isEqualTo(LEGISLATION_PROCESS_STATUS.getValue()),
			() -> assertThat(result.getSummary()).isEqualTo(SUMMARY),
			() -> assertThat(result.getDetail()).isEqualTo(DETAIL),
			() -> assertThat(result.getViewCount()).isEqualTo(VIEW_COUNT)
		);
	}

	@DisplayName("법안 ID 를 받아, 최근법안게시글 상세조회 시 값이 없으면 예외를 발생시킨다")
	@Test
	void readBillPostDetailFailure() {
		BillPostDetailQuery postDetailQuery = new BillPostDetailQuery(BILL_ID_1);
		when(billPostRepository.retrieveRecentBillPost(postDetailQuery))
			.thenReturn(null);

		assertThatThrownBy(() -> billPostReader.readBillPostDetail(postDetailQuery))
			.isInstanceOf(BillPostDomainException.class)
			.hasMessage(BillPostDomainException.notFound(BILL_ID_1).getMessage());
	}
}