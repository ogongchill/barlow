package com.barlow.client.knal.opendata.api.response.common;

public record SuccessHeader(
	String successYN,
	String requestMsgID,
	String responseMsgID,
	String responseTime,
	String resultCode,
	String resultMsg
) implements Header {
}
