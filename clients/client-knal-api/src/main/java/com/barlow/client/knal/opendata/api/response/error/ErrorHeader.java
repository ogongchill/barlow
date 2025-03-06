package com.barlow.client.knal.opendata.api.response.error;

public record ErrorHeader(
    String errMsg,
    String returnAuthMsg,
    String returnReasonCode
) {
}
