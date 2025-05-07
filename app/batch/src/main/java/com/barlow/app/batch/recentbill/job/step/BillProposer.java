package com.barlow.app.batch.recentbill.job.step;

import java.util.List;

import com.barlow.app.batch.recentbill.LawmakerProvider;

public record BillProposer(
	String billId,
	List<LawmakerProvider.Lawmaker> lawmakers
) {
}
