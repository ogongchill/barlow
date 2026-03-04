package com.barlow.batch.admin.bill.job.step;

import com.barlow.core.enumerate.LegislationType;

public record BillLegislationBody(
	String billId,
	LegislationType committee
) {
}
