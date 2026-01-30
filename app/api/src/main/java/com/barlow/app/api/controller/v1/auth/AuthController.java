package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.domain.registration.MemberCreateCommand;
import com.barlow.core.domain.registration.MemberPromoteCommand;
import com.barlow.core.domain.registration.MemberRegisterService;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationRequest;
import com.barlow.services.auth.authentication.oauth.OidcAuthenticationService;
import com.barlow.services.auth.support.annotation.PassportUser;
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

	public AuthController(
            AccountCreateService accountCreateService,
            AccountLoginService accountLoginService,
            AccessTokenProvider accessTokenProvider,
			MemberRegisterService memberRegisterService,
			OidcAuthenticationService oidcAuthenticationService
    ) {
		this.accountCreateService = accountCreateService;
		this.accountLoginService = accountLoginService;
		this.accessTokenProvider = accessTokenProvider;
        this.memberRegisterService = memberRegisterService;
        this.oidcAuthenticationService = oidcAuthenticationService;
    }

	/**
	 * 사용자 편의를 위해 회원가입 시 access token 바로 발급.
	 * 추가적인 로그인 없도록 함
	 */
	@PostMapping("/guest/signup")
	public ApiResponse<LoginResponse> guestSignup(@RequestBody SignupRequest request) {
		log.info("Received guest signup request.");
		request.validate();
		User guest = accountCreateService.createGuest(request.toGuestCommand());
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

	@PostMapping("/oidc/signup")
	public ApiResponse<LoginResponse> oidcSignup(@RequestBody OidcSignupRequest request) {
		log.info("Received oidc signup request.");
		request.signupRequest().validate();
		ExternalPrincipal principal = oidcAuthenticationService.authenticate(new OidcAuthenticationRequest(request.oidcRequest().authProvider(), request.oidcRequest().idToken()));
		MemberCreateCommand command = new MemberCreateCommand(
				principal,
				request.toPayload(),
				request.termAgreementRequest().toTermAgreements(LocalDateTime.now())
		);
		User member = memberRegisterService.createNewMember(command);
		AccessToken accessToken = accessTokenProvider.issue(member);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}

	@PostMapping("/oidc/promote")
	public ApiResponse<LoginResponse> oidcPromote(@PassportUser User existingUser, @RequestBody OidcRolePromoteRequest request) {
		log.info("Received oidc promote request.");
		ExternalPrincipal principal = oidcAuthenticationService.authenticate(new OidcAuthenticationRequest(request.oidcRequest().toAuthProvider(), request.oidcRequest().idToken()));
		MemberPromoteCommand command = new MemberPromoteCommand(
				principal,
				existingUser,
				request.termAgreementRequest().toTermAgreements(LocalDateTime.now())
		);
		User member = memberRegisterService.promoteToMember(command);
		AccessToken accessToken = accessTokenProvider.issue(member);
		return ApiResponse.success(new LoginResponse(accessToken.getValue()));
	}
}
