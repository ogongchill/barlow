package com.barlow.core.api.controller.v1.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.services.auth.authentication.token.AccessToken;
import com.barlow.services.auth.authentication.token.AccessTokenProvider;
import com.barlow.core.domain.User;
import com.barlow.core.domain.account.AccountCreateService;
import com.barlow.core.domain.account.AccountLoginService;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final AccountCreateService accountCreateService;
	private final AccountLoginService accountLoginService;
	private final AccessTokenProvider accessTokenProvider;

	public AuthController(
		AccountCreateService accountCreateService,
		AccountLoginService accountLoginService,
		AccessTokenProvider accessTokenProvider
	) {
		this.accountCreateService = accountCreateService;
		this.accountLoginService = accountLoginService;
		this.accessTokenProvider = accessTokenProvider;
	}

	/**
	 * 사용자 편의를 위해 회원가입 시 access token 바로 발급.
	 * 추가적인 로그인 없도록 함
	 */
	@PostMapping("/guest/signup")
	public ApiResponse<LoginResponse> guestSignup(@RequestBody SignupRequest request) {
		log.info("Received guest signup request.");
		request.validate();
		User guest = accountCreateService.createGuest(request.toCommand());
		AccessToken accessToken = accessTokenProvider.issue(guest);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	/**
	 * access token 만료 시 재 로그인
	 */
	@PostMapping("/guest/login")
	public ApiResponse<LoginResponse> guestLogin(@RequestBody LoginRequest request) {
		log.info("Received guest login request.");
		request.validate();
		User guest = accountLoginService.guestLogin(request.toCommand());
		AccessToken accessToken = accessTokenProvider.issue(guest);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}
}
