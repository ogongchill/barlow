package com.barlow.core.domain.account;

import java.util.List;
import java.util.function.Function;

public class LegislationAccounts {

    private final List<LegislationAccount> legislationAccounts;

    public LegislationAccounts(List<LegislationAccount> legislationAccounts) {
        this.legislationAccounts = legislationAccounts;
    }

    public <T> List<T> map(Function<LegislationAccount, T> mapperFunction) {
        return legislationAccounts.stream()
                .map(mapperFunction)
                .toList();
    }
}
