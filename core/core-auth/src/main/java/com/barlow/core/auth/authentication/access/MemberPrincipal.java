package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.Principal;

public class MemberPrincipal extends Principal {

    private final Long memberNo;
    private final String role;

    public MemberPrincipal(boolean isAuthenticated ,Long memberNo, String role) {
        super(isAuthenticated);
        this.memberNo = memberNo;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static MemberPrincipal notAuthenticated() {
        return new MemberPrincipal(false, -1L, "");
    }

    public Long getMemberNo() {
        return memberNo;
    }
}
