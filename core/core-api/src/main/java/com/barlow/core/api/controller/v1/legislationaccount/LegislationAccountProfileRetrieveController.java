package com.barlow.core.api.controller.v1.legislationaccount;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRetrieveService;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts/{accountNo}/profile")
public class LegislationAccountProfileRetrieveController {

	private final LegislationAccountRetrieveService legislationAccountRetrieveService;

	public LegislationAccountProfileRetrieveController(
		LegislationAccountRetrieveService legislationAccountRetrieveService
	) {
		this.legislationAccountRetrieveService = legislationAccountRetrieveService;
	}

	@GetMapping
	public ApiResponse<LegislationAccountProfileResponse> retrieveProfile(
		@PathVariable Long accountNo,
		User user
	) {
		LegislationAccount legislationAccount = legislationAccountRetrieveService.retrieve(accountNo, user);
		return ApiResponse.success(LegislationAccountProfileResponse.from(legislationAccount));
	}
}
