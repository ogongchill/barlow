package com.barlow.batch.core.recentbill.job;

import java.time.LocalDate;

public interface TodayBillRetrieveClient {

	TodayBillInfoBatchEntity getTodayBillInfo(LocalDate batchDate);

	BillProposerInfoBatchEntity getBillProposerInfo(String billId);
}
