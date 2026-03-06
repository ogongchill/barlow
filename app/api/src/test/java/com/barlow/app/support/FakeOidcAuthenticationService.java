package com.barlow.app.support;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;

import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationRequest;
import com.barlow.infra.auth.authentication.oauth.OidcAuthenticationService;

@TestComponent
@Primary
public class FakeOidcAuthenticationService extends OidcAuthenticationService {

	private static final ExternalPrincipal DEFAULT_PRINCIPAL = new ExternalPrincipal(AuthProvider.KAKAO,
		"test_subject_123");

	private ExternalPrincipal nextPrincipal = DEFAULT_PRINCIPAL;
	private RuntimeException nextException = null;

	public FakeOidcAuthenticationService() {
		super(null);
	}

	@Override
	public ExternalPrincipal authenticate(OidcAuthenticationRequest request) {
		if (nextException != null) {
			RuntimeException ex = nextException;
			nextException = null;
			throw ex;
		}
		return nextPrincipal;
	}

	public void willReturn(ExternalPrincipal principal) {
		this.nextPrincipal = principal;
		this.nextException = null;
	}

	public void willThrow(RuntimeException exception) {
		this.nextException = exception;
	}

	public void reset() {
		this.nextPrincipal = DEFAULT_PRINCIPAL;
		this.nextException = null;
	}
}
