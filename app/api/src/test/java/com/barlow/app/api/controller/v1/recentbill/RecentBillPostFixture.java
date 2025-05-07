package com.barlow.app.api.controller.v1.recentbill;

import java.time.LocalDateTime;
import java.util.List;

import com.barlow.core.domain.billpost.BillProposer;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class RecentBillPostFixture {

	static final String BILL_ID_1 = "bill_id_1";
	static final String BILL_NAME_1 = "bill_name_1";
	static final ProposerType PROPOSER_TYPE = ProposerType.LAWMAKER;
	static final String PROPOSERS_SUMMARY = "홍길동 등 1명";
	static final String PROPOSER_NAME = "홍길동";
	static final String PARTY_NAME = "홍길동";
	static final String PROFILE_IMAGE_PATH = "lawmaker/profile-image/";
	static final LegislationType LEGISLATION_TYPE = LegislationType.EMPTY;
	static final ProgressStatus LEGISLATION_PROCESS_STATUS = ProgressStatus.RECEIVED;
	static final String SUMMARY = "AI 가 요약한 법안 내용";
	static final String DETAIL = "주요내용 및 제안이유";
	static final int VIEW_COUNT = 1;

	static final BillPost RECENT_BILL_POST = new BillPost(
		new BillPost.BillInfo(BILL_ID_1, BILL_NAME_1),
		new BillPost.ProposerInfo(PROPOSER_TYPE, PROPOSERS_SUMMARY),
		new BillPost.LegislationInfo(LEGISLATION_TYPE, LEGISLATION_PROCESS_STATUS),
		SUMMARY, DETAIL, LocalDateTime.of(2024, 12, 31, 23, 59), VIEW_COUNT
	);

	static final BillProposer PROPOSER = new BillProposer("code", PROPOSER_NAME, PARTY_NAME, PROFILE_IMAGE_PATH);
	static final BillPostsStatus RECENT_BILL_POSTS_STATUS = new BillPostsStatus(
		List.of(RECENT_BILL_POST),
		true
	);
}
