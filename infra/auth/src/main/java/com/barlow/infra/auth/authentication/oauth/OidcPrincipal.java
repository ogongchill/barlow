package com.barlow.infra.auth.authentication.oauth;

import com.barlow.infra.auth.authentication.core.Principal;

public class OidcPrincipal extends Principal {

	private final String iss;
	private final String sub;
	private final String aud;
	private final String nickname;
	private final OauthProvider oauthProvider;

	public OidcPrincipal(String iss, String sub, String aud, String nickname, OauthProvider oauthProvider) {
		super(iss + ":" + sub);
		this.iss = iss;
		this.sub = sub;
		this.aud = aud;
		this.nickname = nickname;
		this.oauthProvider = oauthProvider;
	}

	public static Builder ofKakao() {
		return new Builder(OauthProvider.KAKAO);
	}

	public static class Builder {

		private final OauthProvider oauthProvider;
		private String iss;
		private String sub;
		private String aud;
		private String nickname;

		private Builder(OauthProvider oauthProvider) {
			this.oauthProvider = oauthProvider;
		}

		public Builder iss(String iss) {
			this.iss = iss;
			return this;
		}

		public Builder sub(String sub) {
			this.sub = sub;
			return this;
		}

		public Builder aud(String aud) {
			this.aud = aud;
			return this;
		}

		public Builder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public OidcPrincipal build() {
			return new OidcPrincipal(this.iss, this.sub, this.aud, this.nickname, this.oauthProvider);
		}
	}

	public String getIss() {
		return iss;
	}

	public String getSub() {
		return sub;
	}

	public String getAud() {
		return aud;
	}

	public String getNickname() {
		return nickname;
	}

	public OauthProvider getOauthProvider() {
		return oauthProvider;
	}
}
