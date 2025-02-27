package com.barlow.core.auth.authentication.core;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberPrincipal principal = (MemberPrincipal) o;
        return memberNo.equals(principal.memberNo)
               && role.equals( principal.role)
               && identifier.equals(principal.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, role, identifier);
    }
}
