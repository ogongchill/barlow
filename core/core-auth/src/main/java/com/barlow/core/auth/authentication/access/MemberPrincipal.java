package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.Principal;

public class MemberPrincipal extends Principal {

    private final Long memberNo;
    private final String role;

    public MemberPrincipal(Long memberNo, String role) {
        super(memberNo.toString());
        this.memberNo = memberNo;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Long getMemberNo() {
        return memberNo;
    }
}
