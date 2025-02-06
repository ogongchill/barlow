package com.barlow.support.jwt.token;

import com.barlow.core.auth.authorization.AuthorizableMemberInfo;
import com.barlow.core.auth.authorization.MemberAuthorizer;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthorizerAdapter implements MemberAuthorizer {

    private final MemberTokenProvider memberTokenProvider;

    public MemberAuthorizerAdapter(MemberTokenProvider memberTokenProvider) {
        this.memberTokenProvider = memberTokenProvider;
    }

    public String sign(AuthorizableMemberInfo memberInfo) {
        return memberTokenProvider.sign(memberInfo);
    }
}
