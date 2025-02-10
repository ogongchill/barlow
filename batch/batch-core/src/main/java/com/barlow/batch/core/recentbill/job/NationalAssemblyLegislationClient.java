package com.barlow.batch.core.recentbill.job;

import java.time.LocalDate;

public interface NationalAssemblyLegislationClient {

	TodayBillInfoResult getTodayBillInfo(LocalDate batchDate);

	BillProposerInfoResult getBillProposerInfo(String billId);
}
