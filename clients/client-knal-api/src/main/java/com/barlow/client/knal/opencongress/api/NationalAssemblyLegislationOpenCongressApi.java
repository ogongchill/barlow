package com.barlow.client.knal.opencongress.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.barlow.client.knal.opencongress.api.common.DefaultRequest;
import com.barlow.client.knal.opencongress.api.preannounce.PreAnnouncementResponse;

@FeignClient(
	value = "${knal.open-congress.api.name}",
	url = "${knal.open-congress.api.url}",
	configuration = OpenCongressConfig.class)
public interface NationalAssemblyLegislationOpenCongressApi {

	@GetMapping(
		value = Operation.GET_PRE_ANNOUNCEMENT,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	PreAnnouncementResponse getPreAnnouncement(@SpringQueryMap DefaultRequest request);
}
