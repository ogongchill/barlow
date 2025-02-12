package com.barlow.core.auth.principal;

public abstract class Principal {

    private final String identifier;

    protected Principal(String id) {
        this.identifier = id;
    }
}
