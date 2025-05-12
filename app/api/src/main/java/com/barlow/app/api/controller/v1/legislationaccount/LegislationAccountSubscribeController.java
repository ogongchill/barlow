package com.barlow.app.api.controller.v1.legislationaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.legislationaccount.LegislationAccountSubscribeService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.services.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{legislationType}/subscribe")
public class LegislationAccountSubscribeController {

	private static final Logger log = LoggerFactory.getLogger(LegislationAccountSubscribeController.class);

	private final LegislationAccountSubscribeService legislationAccountSubscribeService;

	public LegislationAccountSubscribeController(
		LegislationAccountSubscribeService legislationAccountSubscribeService
	) {
		this.legislationAccountSubscribeService = legislationAccountSubscribeService;
	}

	@PostMapping("/activate")
	public ApiResponse<Void> subscribe(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser Passport passport
	) {
		log.info("Received {} account subscribe request.", legislationType);
		legislationAccountSubscribeService.subscribeAccount(legislationType, passport.getUser());
		return ApiResponse.success();
	}

	@PostMapping("/deactivate")
	public ApiResponse<Void> unsubscribe(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser Passport passport
	) {
		log.info("Received {} account unsubscribe request.", legislationType);
		legislationAccountSubscribeService.unsubscribeAccount(legislationType, passport.getUser());
		return ApiResponse.success();
	}
}
