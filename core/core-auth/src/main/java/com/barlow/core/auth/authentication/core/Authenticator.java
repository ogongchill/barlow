package com.barlow.core.auth.authentication.core;

import com.barlow.core.auth.principal.Principal;

public interface Authenticator<C extends Credential, P extends Principal> {

    P authenticate(C credential);
}
