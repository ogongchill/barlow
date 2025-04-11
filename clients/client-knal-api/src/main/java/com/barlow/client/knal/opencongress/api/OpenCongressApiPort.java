package com.barlow.client.knal.opencongress.api;

import com.barlow.client.knal.opencongress.api.common.DefaultRequest;
import com.barlow.client.knal.opencongress.api.preannounce.PreAnnouncementResponse;

public interface OpenCongressApiPort {
	PreAnnouncementResponse getPreAnnouncement(DefaultRequest request) throws OpenCongressException;
}
