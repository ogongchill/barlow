package com.barlow.core.api.controller.v1.recentbill;

import java.time.LocalDateTime;
import java.util.List;

import com.barlow.core.domain.recentbill.BillProposer;
import com.barlow.core.domain.recentbill.RecentBillPost;
import com.barlow.core.domain.recentbill.RecentBillPostsStatus;

public class RecentBillPostFixture {

	static final String BILL_ID_1 = "bill_id_1";
	static final String BILL_NAME_1 = "bill_name_1";
	static final String PROPOSER_TYPE = "국회의원";
	static final String PROPOSERS_SUMMARY = "홍길동 등 1명";
	static final String PROPOSER_NAME = "홍길동";
	static final String PARTY_NAME = "홍길동";
	static final String PROFILE_IMAGE_PATH = "lawmaker/profile-image/";
	static final String LEGISLATION_TYPE = "소관위미접수상태";
	static final String LEGISLATION_PROCESS_STATUS = "접수";
	static final String SUMMARY = "AI 가 요약한 법안 내용";
	static final String DETAIL = "주요내용 및 제안이유";
	static final int VIEW_COUNT = 1;

	static final RecentBillPost RECENT_BILL_POST = new RecentBillPost(
		new RecentBillPost.BillInfo(BILL_ID_1, BILL_NAME_1),
		new RecentBillPost.ProposerInfo(PROPOSER_TYPE, PROPOSERS_SUMMARY),
		new RecentBillPost.LegislationInfo(LEGISLATION_TYPE, LEGISLATION_PROCESS_STATUS),
		SUMMARY, DETAIL, LocalDateTime.of(2024, 12, 31, 23, 59), VIEW_COUNT
	);

	static final BillProposer PROPOSER = new BillProposer("code", PROPOSER_NAME, PARTY_NAME, PROFILE_IMAGE_PATH);
	static final RecentBillPostsStatus RECENT_BILL_POSTS_STATUS = new RecentBillPostsStatus(
		List.of(RECENT_BILL_POST),
		true
	);
}
