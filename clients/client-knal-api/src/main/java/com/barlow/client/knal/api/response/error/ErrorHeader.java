package com.barlow.client.knal.api.response.error;

public record ErrorHeader(
    String errMsg,
    String returnAuthMsg,
    String returnReasonCode
) {
}
