package com.barlow.client.knal.opendata.api.response.common;

public interface Header {

	String resultCode();

	String resultMsg();

	String requestMsgID();

	String responseMsgID();

	String responseTime();

	String successYN();
}
