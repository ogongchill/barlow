package com.barlow.app.api.controller.v1.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.app.support.response.ApiResponse;
import com.barlow.core.domain.Passport;
import com.barlow.core.domain.account.AccountWithdrawalService;
import com.barlow.services.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	private final AccountWithdrawalService accountWithdrawalService;

	public AccountController(AccountWithdrawalService accountWithdrawalService) {
		this.accountWithdrawalService = accountWithdrawalService;
	}

	@PostMapping("/withdraw")
	public ApiResponse<Void> withdraw(@PassportUser Passport passport) {
		log.info("Received [guest-user:{}, device-id:{}] withdrawal request", passport.getUserNo(), passport.getDeviceId());
		accountWithdrawalService.withdraw(passport);
		return ApiResponse.success();
	}
}
