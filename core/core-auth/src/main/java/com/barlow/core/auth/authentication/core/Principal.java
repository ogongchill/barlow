package com.barlow.core.auth.authentication.core;

public abstract class Principal {

    protected final String identifier;

    protected Principal(String id) {
        this.identifier = id;
    }
}
