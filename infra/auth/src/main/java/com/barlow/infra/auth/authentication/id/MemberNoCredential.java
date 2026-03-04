package com.barlow.infra.auth.authentication.id;

import com.barlow.infra.auth.authentication.core.Credential;
import com.barlow.infra.auth.authentication.core.CredentialType;

public class MemberNoCredential extends Credential {

    private final long memberNo;

    protected MemberNoCredential(long memberNo) {
        super(CredentialType.ID);
        this.memberNo = memberNo;
    }

    public long getMemberNo() {
        return memberNo;
    }
}
