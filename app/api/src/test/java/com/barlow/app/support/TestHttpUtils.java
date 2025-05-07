package com.barlow.app.support;

import java.util.Map;

public class TestHttpUtils {

	private TestHttpUtils() {
	}

	public static final String AUTHORIZATION = "Authorization";
	public static final String AUTHENTICATION_TYPE = "Bearer ";
	public static final String X_CLIENT_OS = "X-Client-OS";
	public static final String X_CLIENT_OS_VERSION = "X-Client-OS-Version";
	public static final String X_DEVICE_ID = "X-Device-ID";

	public static final Map<String, String> MANDATORY_DEVICE_HEADERS = Map.of(
		X_CLIENT_OS, "ios",
		X_CLIENT_OS_VERSION, "device_os_version",
		X_DEVICE_ID, "device_id_value_1"
	);
}
