package com.barlow.core.auth.authentication.id;

import com.barlow.core.auth.authentication.core.Authenticator;
import com.barlow.core.auth.authentication.core.MemberPrincipal;
import org.springframework.stereotype.Component;

@Component
public class MemberNoAuthenticator implements Authenticator<MemberNoCredential, MemberPrincipal> {

    private final MemberRepository memberRepository;

    public MemberNoAuthenticator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberPrincipal authenticate(MemberNoCredential credential) {
        return memberRepository.findByMemberNo(credential.getMemberNo())
                .orElseThrow(() -> MemberNoAuthenticationException.memberNotFound(credential.getMemberNo()));
    }
}
