package com.barlow.services.auth.authentication.id;

import com.barlow.services.auth.authentication.core.Credential;
import com.barlow.services.auth.authentication.core.CredentialType;

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
