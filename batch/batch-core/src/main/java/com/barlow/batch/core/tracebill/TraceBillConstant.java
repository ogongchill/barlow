package com.barlow.batch.core.tracebill;

public final class TraceBillConstant {

	private TraceBillConstant() {
	}

	public static final String JOB_NAME = "traceBillJob";
	public static final String FIRST_STEP_NAME = "traceBillDirtyCheckStep";
	public static final String SECOND_STEP_NAME = "traceBillUpdateStep";
	public static final String THIRD_STEP_NAME = "traceBillNotifyStep";

	public static final String TRACKING_END_DATE_JOB_PARAMETER = "yesterdayDate";
	public static final String TRACKING_START_DATE_JOB_PARAMETER = "startProposeDate";

	public static final String UPDATED_BILL_SHARE_KEY = "updatedBills";
}
