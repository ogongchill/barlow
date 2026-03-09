package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.account.MemberLoginCommand;
import com.barlow.core.domain.account.MemberCreateCommand;
import com.barlow.core.domain.account.MemberPromoteCommand;
import com.barlow.core.service.account.business.MemberRegisterService;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationService;
import com.barlow.infra.auth.support.annotation.PassportUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.infra.auth.authentication.token.AccessToken;
import com.barlow.infra.auth.authentication.token.AccessTokenProvider;
import com.barlow.core.domain.User;
import com.barlow.core.service.account.business.AccountCreateService;
import com.barlow.core.service.account.business.AccountLoginService;
import com.barlow.app.support.response.ApiResponse;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final AccountCreateService accountCreateService;
	private final AccountLoginService accountLoginService;
	private final AccessTokenProvider accessTokenProvider;
	private final MemberRegisterService memberRegisterService;
	private final OidcAuthenticationService oidcAuthenticationService;

	public AuthController(AccountCreateService accountCreateService, AccountLoginService accountLoginService,
		AccessTokenProvider accessTokenProvider, MemberRegisterService memberRegisterService,
		OidcAuthenticationService oidcAuthenticationService) {
		this.accountCreateService = accountCreateService;
		this.accountLoginService = accountLoginService;
		this.accessTokenProvider = accessTokenProvider;
		this.memberRegisterService = memberRegisterService;
		this.oidcAuthenticationService = oidcAuthenticationService;
	}

	/**
	 * 게스트 계정 생성 (회원가입). 생성 직후 access token 즉시 발급.
	 */
	@PostMapping("/guests")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<LoginResponse> guestSignup(@RequestBody SignupRequest request) {
		log.info("Received guest signup request.");
		request.validate();
		User guest = accountCreateService
			.createGuest(request.toGuestCommand(), request.toTermAgreements(LocalDateTime.now()));
		AccessToken accessToken = accessTokenProvider.issue(guest);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	/**
	 * 게스트 세션 생성 (로그인). access token 만료 시 재발급.
	 */
	@PostMapping("/guest/sessions")
	public ApiResponse<LoginResponse> guestLogin(@RequestBody LoginRequest request) {
		log.info("Received guest login request.");
		request.validate();
		User guest = accountLoginService.guestLogin(request.toCommand());
		AccessToken accessToken = accessTokenProvider.issue(guest);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	/**
	 * OIDC 멤버 계정 생성 (회원가입). 생성 직후 access token 즉시 발급.
	 */
	@PostMapping("/oidc/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<LoginResponse> oidcSignup(@RequestBody OidcSignupRequest request) {
		log.info("Received oidc signup request.");
		request.signupPayload().validate();
		MemberCreateCommand command = request.toCommand(oidcAuthenticationService::authenticate, LocalDateTime.now());
		User member = memberRegisterService.createNewMember(command);
		AccessToken accessToken = accessTokenProvider.issue(member);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	/**
	 * OIDC 인증으로 게스트 → 멤버 역할 전환.
	 */
	@PatchMapping("/oidc/role")
	public ApiResponse<LoginResponse> oidcPromote(@PassportUser Passport passport,
		@RequestBody OidcRolePromoteRequest request) {
		log.info("Received oidc promote request.");
		MemberPromoteCommand command = request.toCommand(passport::getUser, oidcAuthenticationService::authenticate);
		User member = memberRegisterService.promoteToMember(command);
		AccessToken accessToken = accessTokenProvider.issue(member);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	/**
	 * OIDC 세션 생성 (로그인).
	 */
	@PostMapping("/oidc/sessions")
	public ApiResponse<LoginResponse> oidcLogin(@RequestBody OidcLoginRequest request) {
		log.info("Received oidc login request.");
		MemberLoginCommand command = request.toCommand(oidcAuthenticationService::authenticate);
		User member = accountLoginService.memberLogin(command);
		AccessToken accessToken = accessTokenProvider.issue(member);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}
}
