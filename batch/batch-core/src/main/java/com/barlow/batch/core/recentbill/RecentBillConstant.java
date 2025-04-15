package com.barlow.batch.core.recentbill;

public final class RecentBillConstant {

	private RecentBillConstant() {
	}

	public static final String JOB_NAME = "todayBillCreateBatchJob";
	public static final String WRITE_TODAY_BILL_INFO_STEP = "writeTodayBillInfoStep";
	public static final String WRITE_BILL_PROPOSER_STEP = "writeBillProposerStep";
	public static final String TODAY_BILL_NOTIFY_STEP = "todayBillNotifyStep";

	public static final String BATCH_DATE_JOB_PARAMETER = "batchDate";

	public static final String TODAY_BILL_INFO_SHARE_KEY = "TODAY_BILL_INFO";
	public static final String BILL_WITH_FEW_PROPOSERS_SHARE_KEY = "RECEIVED_BILL_WITH_FEW_PROPOSERS";
}
