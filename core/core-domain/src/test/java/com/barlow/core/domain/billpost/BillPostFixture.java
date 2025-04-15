package com.barlow.core.domain.billpost;

import java.time.LocalDateTime;
import java.util.List;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class BillPostFixture {

	static final BillPostsStatus EMPTY_RECENT_BILL_POST_STATUS = new BillPostsStatus(List.of(), true);

	static final String BILL_ID_1 = "bill_id_1";
	static final String BILL_NAME_1 = "bill_name_1";
	static final ProposerType PROPOSER_TYPE = ProposerType.LAWMAKER;
	static final String PROPOSERS = "홍길동 등 10명";
	static final LegislationType LEGISLATION_TYPE = LegislationType.EMPTY;
	static final ProgressStatus LEGISLATION_PROCESS_STATUS = ProgressStatus.RECEIVED;
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
