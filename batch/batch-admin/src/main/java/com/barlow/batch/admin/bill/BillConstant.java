package com.barlow.batch.admin.bill;

public final class BillConstant {

	private BillConstant() {
	}

	public static final String START_DATE_JOB_PARAMETER = "startDate";
	public static final String END_DATE_JOB_PARAMETER = "endDate";

	public static final String BILL_INFO_SHARE_KEY = "BILL_INFO";

	public static final String BILL_WITH_FEW_PROPOSERS_SHARE_KEY = "BILL_WITH_FEW_PROPOSERS";
	public static final String BILL_PROPOSER_READER_INDEX_KEY = "AdminBillProposerReader.currentIndex";

	public static final String BILL_WITH_LEGISLATION_BODY_SHARE_KEY = "BILL_WITH_LEGISLATION_BODY_SHARE_KEY";
	public static final String BILL_LEGISLATION_READER_INDEX_KEY = "BillLegislationReader.currentIndex";
}
