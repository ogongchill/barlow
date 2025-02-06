package com.barlow.core.auth.authorization;

import org.springframework.stereotype.Component;

@Component
public interface MemberAuthorizer {

    String sign(AuthorizableMemberInfo authorizableMemberInfo);
}
