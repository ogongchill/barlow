package com.barlow.infra.auth.authentication.oauth;

import com.barlow.infra.auth.authentication.core.CredentialType;

public class KakaoIdToken extends IdToken {

	protected KakaoIdToken(String value) {
		super(CredentialType.KAKAO_ID_TOKEN, value);
	}

	public static KakaoIdToken of(String value) {
		return new KakaoIdToken(value);
	}
}
