package com.barlow.core.api.controller.v1.home;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.HomeRetrieveFacade;
import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/home")
public class HomeRetrieveController {

	private static final Logger log = LoggerFactory.getLogger(HomeRetrieveController.class);

	private final HomeRetrieveFacade homeRetrieveFacade;

	public HomeRetrieveController(HomeRetrieveFacade homeRetrieveFacade) {
		this.homeRetrieveFacade = homeRetrieveFacade;
	}

	@GetMapping
	public ApiResponse<HomeResponse> retrieveHome(@PassportUser User user) {
		log.info("Received retrieving home information request.");
		LocalDate today = LocalDate.now();
		HomeResponseApiSpecComposer apiSpecComposer = new HomeResponseApiSpecComposer(
			homeRetrieveFacade.retrieveHome(user),
			homeRetrieveFacade.retrieveTodayBillPostThumbnail(today)
		);
		return ApiResponse.success(apiSpecComposer.compose(today));
	}

	@GetMapping("/notification-center")
	public ApiResponse<NotificationCenterResponse> retrieveNotificationCenter(
		@PassportUser User user,
		@RequestParam(name = "filterTopic", required = false) NotificationTopic filterTopic
	) {
		log.info("Received retrieving notification center request.");
		NotificationCenterApiSpecComposer notificationCenterApiSpecComposer = new NotificationCenterApiSpecComposer(
			homeRetrieveFacade.retrieveNotificationCenter(user)
		);
		return ApiResponse.success(notificationCenterApiSpecComposer.compose(filterTopic));
	}
}
