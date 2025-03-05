package com.barlow.client.knal.opencongress.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
	value = "${knal.open-congress.api.name}",
	url = "${knal.open-congress.api.url}",
	configuration = OpenCongressConfig.class)
public interface NationalAssemblyLegislationOpenCongressApi {
}
