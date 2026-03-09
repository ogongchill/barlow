package com.barlow.infra.knal.opencongress.api;

import com.barlow.infra.knal.opencongress.api.common.DefaultRequest;
import com.barlow.infra.knal.opencongress.api.preannounce.PreAnnouncementResponse;

public interface OpenCongressApiPort {
	PreAnnouncementResponse getPreAnnouncement(DefaultRequest request) throws OpenCongressException;
}
