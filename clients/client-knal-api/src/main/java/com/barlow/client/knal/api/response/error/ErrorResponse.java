package com.barlow.client.knal.api.response.error;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record ErrorResponse(
    @JacksonXmlProperty(localName = "cmmMsgHeader")
    ErrorHeader errorHeader
) {
}
