package com.barlow.batch.core.recentbill.job.step;

import java.util.List;

import com.barlow.batch.core.recentbill.LawmakerProvider;

public record BillProposer(
	String billId,
	List<LawmakerProvider.Lawmaker> lawmakers
) {
}
