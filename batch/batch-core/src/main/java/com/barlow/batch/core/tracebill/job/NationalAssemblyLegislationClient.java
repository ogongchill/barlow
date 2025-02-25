package com.barlow.batch.core.tracebill.job;

import java.time.LocalDate;

import com.barlow.core.enumerate.LegislationType;

public interface NationalAssemblyLegislationClient {
	CurrentBillInfoResult getTraceBillInfo(LocalDate startProposeDate, LocalDate batchDate);
	LegislationType getCommittee(String billId);
}
