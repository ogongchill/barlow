package com.barlow.core.domain.account;

import java.util.List;

public class LegislationAccounts {

    private final List<LegislationAccount> legislationAccounts;

    public LegislationAccounts(List<LegislationAccount> legislationAccounts) {
        this.legislationAccounts = List.copyOf(legislationAccounts);
    }

    public List<LegislationAccount> getAll() {
        return List.copyOf(legislationAccounts);
    }
}