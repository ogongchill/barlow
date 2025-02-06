package com.barlow.core.auth.authentication;

public class MemberAuthenticatorResult extends AuthenticationResult<AuthenticatedMember> {

    public MemberAuthenticatorResult(AuthenticationResultType resultType, AuthenticatedMember data) {
        super(resultType, data);
    }

    public static MemberAuthenticatorResult invalid() {
        return new MemberAuthenticatorResult(AuthenticationResultType.INVALID, new AuthenticatedMember("", ""));
    }

    public static MemberAuthenticatorResult expired() {
        return new MemberAuthenticatorResult(AuthenticationResultType.EXPIRED, new AuthenticatedMember("", ""));
    }

    public static MemberAuthenticatorResult ok(AuthenticatedMember authenticatedMemberInfo) {
        return new MemberAuthenticatorResult(AuthenticationResultType.OK, authenticatedMemberInfo);
    }
}
