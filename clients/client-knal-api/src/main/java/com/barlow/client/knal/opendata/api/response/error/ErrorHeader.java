package com.barlow.client.knal.opendata.api.response.error;

import com.barlow.client.knal.opendata.api.response.common.Header;

public record ErrorHeader(
	String errMsg,
	String returnAuthMsg,
	String returnReasonCode
) implements Header {

	@Override
	public String resultCode() {
		return returnReasonCode;
	}

	@Override
	public String resultMsg() {
		return errMsg;
	}

	@Override
	public String requestMsgID() {
		return "";
	}

	@Override
	public String responseMsgID() {
		return "";
	}

	@Override
	public String responseTime() {
		return "";
	}

	@Override
	public String successYN() {
		return "";
	}
}
