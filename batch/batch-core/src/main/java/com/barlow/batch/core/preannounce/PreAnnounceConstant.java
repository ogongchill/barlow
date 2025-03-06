package com.barlow.batch.core.preannounce;

public final class PreAnnounceConstant {

	private PreAnnounceConstant() {
	}

	public static final String JOB_NAME = "preAnnounceBillBatchJob";
	public static final String DIRTY_CHECK_STEP = "preAnnounceBillDirtyCheckStep";
	public static final String WRITE_STEP = "preAnnounceBillWriteStep";

	public static final String NEW_PRE_ANNOUNCE_BILL_SHARE_KEY = "newPreAnnounceBills";
}
