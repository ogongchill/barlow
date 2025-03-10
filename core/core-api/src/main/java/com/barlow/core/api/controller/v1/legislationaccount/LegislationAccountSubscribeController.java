package com.barlow.core.api.controller.v1.legislationaccount;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccountSubscribeService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{legislationType}/subscribe")
public class LegislationAccountSubscribeController {

	private final LegislationAccountSubscribeService legislationAccountSubscribeService;

	public LegislationAccountSubscribeController(
		LegislationAccountSubscribeService legislationAccountSubscribeService
	) {
		this.legislationAccountSubscribeService = legislationAccountSubscribeService;
	}

	@PostMapping("/activate")
	public ApiResponse<Void> subscribe(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		legislationAccountSubscribeService.subscribeAccount(legislationType, user);
		return ApiResponse.success();
	}

	@PostMapping("/deactivate")
	public ApiResponse<Void> unsubscribe(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser User user
	) {
		legislationAccountSubscribeService.unsubscribeAccount(legislationType, user);
		return ApiResponse.success();
	}
}
