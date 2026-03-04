package com.barlow.infra.knal.opendata.api.response;

import com.barlow.infra.knal.opendata.api.response.common.ItemResponseBody;
import com.barlow.infra.knal.opendata.api.response.common.OpenDataResponse;
import com.barlow.infra.knal.opendata.api.response.common.SuccessHeader;
import com.barlow.infra.knal.opendata.api.response.item.MotionLawListItem;

public record MotionLawListResponse(
	SuccessHeader header,
	ItemResponseBody<MotionLawListItem> body
) implements OpenDataResponse<MotionLawListItem> {
}
