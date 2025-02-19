package com.barlow.core.domain.billpost;

import java.time.LocalDateTime;
import java.util.List;

public class BillPostFixture {

	static final BillPostsStatus EMPTY_RECENT_BILL_POST_STATUS = new BillPostsStatus(List.of(), true);

	static final String BILL_ID_1 = "bill_id_1";
	static final String BILL_NAME_1 = "bill_name_1";
	static final String PROPOSER_TYPE = "국회의원";
	static final String PROPOSERS = "홍길동 등 10명";
	static final String LEGISLATION_TYPE = "소관위미접수상태";
	static final String LEGISLATION_PROCESS_STATUS = "접수";
	static final String SUMMARY = "AI 가 요약한 법안 내용";
	static final String DETAIL = "주요내용 및 제안이유";
	static final int VIEW_COUNT = 1;

	static final BillPost RECENT_BILL_POST_1 = new BillPost(
		new BillPost.BillInfo(BILL_ID_1, BILL_NAME_1),
		new BillPost.ProposerInfo(PROPOSER_TYPE, PROPOSERS),
		new BillPost.LegislationInfo(LEGISLATION_TYPE, LEGISLATION_PROCESS_STATUS),
		SUMMARY, DETAIL, LocalDateTime.of(2025, 1, 1, 0, 0), VIEW_COUNT
	);
}
