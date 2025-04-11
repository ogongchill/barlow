package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.client.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.client.knal.opendata.api.response.item.BillInfoListItem;

public record BillInfoListResponse(
	SuccessHeader header,
	ItemResponseBody<BillInfoListItem> body
) implements OpenDataResponse<BillInfoListItem> {
}
