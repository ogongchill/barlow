package com.barlow.app.api.controller.v1.legislationaccount;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRetrieveService;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.services.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/legislation-accounts")
public class LegislationAccountRetrieveController {

	private static final Logger log = LoggerFactory.getLogger(LegislationAccountRetrieveController.class);

	private final LegislationAccountRetrieveService legislationAccountRetrieveService;

	public LegislationAccountRetrieveController(
		LegislationAccountRetrieveService legislationAccountRetrieveService
	) {
		this.legislationAccountRetrieveService = legislationAccountRetrieveService;
	}

	@GetMapping("/{legislationType}/profile")
	public ApiResponse<LegislationAccountProfileResponse> retrieveProfile(
		@PathVariable("legislationType") LegislationType legislationType,
		@PassportUser Passport passport
	) {
		log.info("Received {} account profile retrieve request.", legislationType);
		LegislationAccount legislationAccount = legislationAccountRetrieveService.retrieve(legislationType, passport.getUser());
		return ApiResponse.success(LegislationAccountProfileResponse.from(legislationAccount));
	}

	@GetMapping("/committees/info")
	public ApiResponse<CommitteeAccountResponse> retrieveCommitteeAccounts(@PassportUser Passport passport) {
		log.info("Received user {} committee account info retrieve request.", passport.getUserNo());
		List<LegislationAccount> legislationAccounts = legislationAccountRetrieveService.retrieveAllCommittees(passport.getUser());
		return ApiResponse.success(CommitteeAccountResponse.from(legislationAccounts));
	}
}
