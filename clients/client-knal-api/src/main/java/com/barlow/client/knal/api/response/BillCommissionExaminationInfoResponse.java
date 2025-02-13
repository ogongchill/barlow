package com.barlow.client.knal.api.response;

import com.barlow.client.knal.api.response.common.DetailHeader;

public record BillCommissionExaminationInfoResponse(
	DetailHeader header,
	BillCommissionExaminationInfoBody body
) {
}