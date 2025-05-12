package com.barlow.services.auth.support.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.barlow.services.auth.support.Passport;
import com.barlow.services.auth.authentication.core.MemberPrincipal;
import com.barlow.services.auth.authentication.token.AccessToken;
import com.barlow.services.auth.authentication.token.AccessTokenAuthenticator;
import com.barlow.services.auth.support.error.CoreAuthErrorType;
import com.barlow.services.auth.support.error.CoreAuthException;
import com.barlow.core.domain.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InboundJwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHENTICATION_TYPE = "Bearer ";
	private static final String X_CLIENT_OS = "X-Client-OS";
	private static final String X_CLIENT_OS_VERSION = "X-Client-OS-Version";
	private static final String X_DEVICE_ID = "X-Device-ID";

	private final AccessTokenAuthenticator accessTokenAuthenticator;

	public InboundJwtAuthenticationFilter(AccessTokenAuthenticator accessTokenAuthenticator) {
		this.accessTokenAuthenticator = accessTokenAuthenticator;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String bearerAccessTokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);
		checkHeaderHasToken(bearerAccessTokenValue);
		checkIsBearerType(bearerAccessTokenValue);

		AccessToken accessToken = new AccessToken(bearerAccessTokenValue.substring(AUTHENTICATION_TYPE.length()));
		MemberPrincipal memberPrincipal = accessTokenAuthenticator.authenticate(accessToken);
		User user = User.of(memberPrincipal.getMemberNo(), memberPrincipal.getRole());

		Passport passport = Passport.create(
			user,
			request.getHeader(X_DEVICE_ID),
			request.getHeader(X_CLIENT_OS_VERSION),
			request.getHeader(X_CLIENT_OS)
		);
		request.setAttribute("passport", passport);

		filterChain.doFilter(request, response);
	}

	private void checkHeaderHasToken(String bearerAccessTokenValue) {
		if (!StringUtils.hasText(bearerAccessTokenValue)) {
			throw new CoreAuthException(CoreAuthErrorType.BAD_REQUEST, "Bearer token is empty");
		}
	}

	private void checkIsBearerType(String bearerAccessTokenValue) {
		if (!bearerAccessTokenValue.startsWith(AUTHENTICATION_TYPE)) {
			throw new CoreAuthException(
				CoreAuthErrorType.UNAUTHORIZED,
				"Bearer token does not start with " + AUTHENTICATION_TYPE
			);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.startsWith("/api/v1/auth/guest/signup")
			|| requestURI.startsWith("/api/v1/auth/guest/login")
			|| requestURI.startsWith("/health")
			|| requestURI.startsWith("/h2-console")
			|| requestURI.startsWith("/actuator");
	}
}
