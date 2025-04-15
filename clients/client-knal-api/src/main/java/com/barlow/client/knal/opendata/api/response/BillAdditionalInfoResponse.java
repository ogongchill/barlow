package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.GenericItemBody;
import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.client.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.client.knal.opendata.api.response.item.BillAdditionalInfoItem;

public record BillAdditionalInfoResponse(
	SuccessHeader header,
	GenericItemBody<BillAdditionalInfoItem> body
) implements OpenDataResponse<BillAdditionalInfoItem> {
}

