package com.barlow.batch.core.recentbill;

public final class RecentBillConstant {

	private RecentBillConstant() {
	}

	public static final String JOB_NAME = "todayBillCreateBatchJob";
	public static final String FIRST_STEP_NAME = "writeTodayBillInfoStep";
	public static final String SECOND_STEP_NAME = "writeBillProposerStep";
	public static final String THIRD_STEP_NAME = "todayBillNotifyStep";

	public static final String BATCH_DATE_JOB_PARAMETER = "batchDate";

	public static final String TODAY_BILL_INFO_JOB_KEY = "TODAY_BILL_INFO";
	public static final String RECEIVED_BILL_WITH_FEW_PROPOSERS_JOB_KEY = "RECEIVED_BILL_WITH_FEW_PROPOSERS";
}
