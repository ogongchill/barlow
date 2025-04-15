package com.barlow.client.knal.opendata.api.response;

import com.barlow.client.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.client.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.client.knal.opendata.api.response.item.SessionRequestListItem;

public record SessionRequestListResponse(
	SuccessHeader header,
	ItemResponseBody<SessionRequestListItem> body
) implements OpenDataResponse<SessionRequestListItem> {
}
