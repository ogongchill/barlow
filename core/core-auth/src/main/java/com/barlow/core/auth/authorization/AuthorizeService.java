package com.barlow.core.auth.authorization;

import org.springframework.stereotype.Service;

@Service
public class AuthorizeService {

    private final MemberAuthorizer memberAuthorizer;

    public AuthorizeService(MemberAuthorizer memberAuthorizer) {
        this.memberAuthorizer = memberAuthorizer;
    }

    public String sign(AuthorizableMemberInfo info) {
        return memberAuthorizer.sign(info);
    }
}
