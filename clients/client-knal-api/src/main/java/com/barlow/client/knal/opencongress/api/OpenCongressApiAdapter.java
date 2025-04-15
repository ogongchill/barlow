package com.barlow.client.knal.opencongress.api;

import org.springframework.stereotype.Component;

import com.barlow.client.knal.opencongress.api.common.DefaultRequest;
import com.barlow.client.knal.opencongress.api.preannounce.PreAnnouncementResponse;

@Component
public class OpenCongressApiAdapter implements OpenCongressApiPort {

	private final NationalAssemblyLegislationOpenCongressApi api;

	public OpenCongressApiAdapter(NationalAssemblyLegislationOpenCongressApi api) {
		this.api = api;
	}

	@Override
	public PreAnnouncementResponse getPreAnnouncement(DefaultRequest request) throws OpenCongressException {
		return api.getPreAnnouncement(request);
	}
}
