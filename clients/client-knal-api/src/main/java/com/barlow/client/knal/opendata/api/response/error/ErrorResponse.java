package com.barlow.client.knal.opendata.api.response.error;

import com.barlow.client.knal.opendata.api.response.common.OpenDataResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record ErrorResponse(
    @JacksonXmlProperty(localName = "cmmMsgHeader")
    ErrorHeader header,
    ErrorEmptyBody body
) implements OpenDataResponse<Void> {
}
