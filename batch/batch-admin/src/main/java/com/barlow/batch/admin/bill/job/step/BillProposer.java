package com.barlow.batch.admin.bill.job.step;

import java.util.List;

import com.barlow.batch.admin.bill.LawmakerProvider;

public record BillProposer(
	String billId,
	List<LawmakerProvider.Lawmaker> lawmakers
) {
}
