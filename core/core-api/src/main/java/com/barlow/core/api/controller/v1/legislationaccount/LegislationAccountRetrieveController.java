package com.barlow.core.api.controller.v1.legislationaccount;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRetrieveService;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts")
public class LegislationAccountRetrieveController {

	private final LegislationAccountRetrieveService legislationAccountRetrieveService;

	public LegislationAccountRetrieveController(
		LegislationAccountRetrieveService legislationAccountRetrieveService
	) {
		this.legislationAccountRetrieveService = legislationAccountRetrieveService;
	}

	@GetMapping("/{accountNo}/profile")
	public ApiResponse<LegislationAccountProfileResponse> retrieveProfile(
		@PathVariable Long accountNo,
		@PassportUser User user
	) {
		LegislationAccount legislationAccount = legislationAccountRetrieveService.retrieve(accountNo, user);
		return ApiResponse.success(LegislationAccountProfileResponse.from(legislationAccount));
	}

	@GetMapping("/committees/info")
	public ApiResponse<CommitteeAccountResponse> retrieveCommitteeAccounts(@PassportUser User user) {
		List<LegislationAccount> legislationAccounts = legislationAccountRetrieveService.retrieveAllCommittees(user);
		return ApiResponse.success(CommitteeAccountResponse.from(legislationAccounts));
	}
}
