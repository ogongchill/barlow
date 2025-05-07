package com.barlow.app.batch.tracebill;

public final class TraceBillConstant {

	private TraceBillConstant() {
	}

	public static final String JOB_NAME = "traceBillJob";
	public static final String TRACE_BILL_DIRTY_CHECK_STEP = "traceBillDirtyCheckStep";
	public static final String TRACE_BILL_UPDATE_STEP = "traceBillUpdateStep";
	public static final String TRACE_BILL_NOTIFY_STEP = "traceBillNotifyStep";

	public static final String TRACKING_END_DATE_JOB_PARAMETER = "yesterdayDate";
	public static final String TRACKING_START_DATE_JOB_PARAMETER = "startProposeDate";

	public static final String UPDATED_BILL_SHARE_KEY = "updatedBills";
}
