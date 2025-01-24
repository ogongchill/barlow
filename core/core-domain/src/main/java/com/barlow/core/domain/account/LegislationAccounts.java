package com.barlow.core.domain.account;

import java.util.List;
import java.util.function.Function;

public class LegislationAccounts {

    private final List<LegislationAccount> legislationAccounts;

    public LegislationAccounts(List<LegislationAccount> legislationAccounts) {
        this.legislationAccounts = List.copyOf(legislationAccounts);
    }

    public <T> List<T> compose(Function<LegislationAccount, T> composeFunction) {
        return legislationAccounts.stream()
                .map(composeFunction)
                .toList();
    }
}