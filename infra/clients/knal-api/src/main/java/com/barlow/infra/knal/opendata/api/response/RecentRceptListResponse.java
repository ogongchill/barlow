package com.barlow.infra.knal.opendata.api.response;

import com.barlow.infra.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.infra.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.infra.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.infra.knal.opendata.api.response.item.RecentRceptListItem;

public record RecentRceptListResponse(SuccessHeader header,
	ItemResponseBody<RecentRceptListItem> body) implements OpenDataResponse<RecentRceptListItem> {
}
