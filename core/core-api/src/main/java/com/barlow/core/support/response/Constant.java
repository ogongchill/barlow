package com.barlow.core.support.response;

public final class Constant {

	private Constant() {
	}

	public static final String IMAGE_SYSTEM_PROPERTY = "image.access.url";
	public static final String IMAGE_ACCESS_URL = System.getProperty(IMAGE_SYSTEM_PROPERTY);
}