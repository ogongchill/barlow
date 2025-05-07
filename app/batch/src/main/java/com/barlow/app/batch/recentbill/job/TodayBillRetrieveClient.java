package com.barlow.app.batch.recentbill.job;

import java.time.LocalDate;

public interface TodayBillRetrieveClient {

	TodayBillInfoBatchEntity getTodayBillInfo(LocalDate batchDate);

	BillProposerInfoBatchEntity getBillProposerInfo(String billId);
}
