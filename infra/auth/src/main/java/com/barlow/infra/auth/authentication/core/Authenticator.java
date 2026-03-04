package com.barlow.infra.auth.authentication.core;

public interface Authenticator<C extends Credential, P extends Principal> {

	P authenticate(C credential);
}
