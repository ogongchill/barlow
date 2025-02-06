package com.barlow.core.auth.authentication.core;

public abstract class Principal {

    private final boolean isAuthenticated;

    protected Principal(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
