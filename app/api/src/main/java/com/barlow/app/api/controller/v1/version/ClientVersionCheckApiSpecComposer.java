package com.barlow.app.api.controller.v1.version;

import com.barlow.core.enumerate.ClientVersionStatus;

public class ClientVersionCheckApiSpecComposer {

	private final ClientVersionStatus clientVersionStatus;

	public ClientVersionCheckApiSpecComposer(ClientVersionStatus clientVersionStatus) {
		this.clientVersionStatus = clientVersionStatus;
	}

	ClientVersionCheckResponse compose() {
		if (clientVersionStatus.equals(ClientVersionStatus.LATEST)) {
			return new ClientVersionCheckResponse(false, false);
		}
		if (clientVersionStatus.equals(ClientVersionStatus.UPDATE_AVAILABLE)) {
			return new ClientVersionCheckResponse(false, true);
		}
		return new ClientVersionCheckResponse(true, true);
	}
}
