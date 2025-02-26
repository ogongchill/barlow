package com.barlow.batch.core.tracebill.job;

import java.time.LocalDate;

import com.barlow.core.enumerate.LegislationType;

public interface BillTrackingClient {

	CurrentBillInfoResult getTraceBillInfo(LocalDate startProposeDate, LocalDate batchDate);

	LegislationType getCommittee(String billId);
}
