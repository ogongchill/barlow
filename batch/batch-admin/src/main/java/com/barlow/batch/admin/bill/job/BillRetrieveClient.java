package com.barlow.batch.admin.bill.job;

import java.time.LocalDate;

import com.barlow.core.enumerate.LegislationType;

public interface BillRetrieveClient {

	BillInfoBatchEntity getAllBillInfo(LocalDate startDate, LocalDate endDate);

	BillProposerInfoResult getBillProposerInfo(String billId);

	LegislationType getCommittee(String billId);
}
