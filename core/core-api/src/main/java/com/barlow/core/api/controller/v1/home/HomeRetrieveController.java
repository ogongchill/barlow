package com.barlow.core.api.controller.v1.home;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.HomeRetrieveService;
import com.barlow.core.domain.home.HomeStatus;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/home")
public class HomeRetrieveController {

	private final HomeRetrieveService homeRetrieveService;

	public HomeRetrieveController(HomeRetrieveService homeRetrieveService) {
		this.homeRetrieveService = homeRetrieveService;
	}

	@GetMapping
	public ApiResponse<HomeResponse> retrieveHome(User user) {
		HomeStatus homeStatus = homeRetrieveService.retrieveHome(user);
		List<HomeResponse.SubscribeLegislationBody> subscribeLegislationBodies = homeStatus.myLegislationAccounts()
			.stream()
			.map(status -> new HomeResponse.SubscribeLegislationBody(
				status.getNo(),
				status.getBodyType(),
				status.getIconImagePath()
			))
			.toList();
		HomeResponse homeResponse = new HomeResponse(
			homeStatus.isNotificationArrived(),
			HomeResponse.SubscribeSection.from(subscribeLegislationBodies)
		);
		return ApiResponse.success(homeResponse);
	}
}
