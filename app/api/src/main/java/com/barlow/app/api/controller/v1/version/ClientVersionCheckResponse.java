package com.barlow.app.api.controller.v1.version;

public record ClientVersionCheckResponse(
	boolean needForceUpdate,
	boolean updateAvailable
) {
}
