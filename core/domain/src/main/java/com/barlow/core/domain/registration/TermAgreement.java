package com.barlow.core.domain.registration;

import java.time.LocalDateTime;

public record TermAgreement(
        Long termId,
        boolean agreed,
        LocalDateTime agreedAt
) {

    public static TermAgreement agree(Term term) {
        return new TermAgreement(
                term.id(),
                true,
                LocalDateTime.now()
        );
    }

    public static TermAgreement disagree(Term term) {
        return new TermAgreement(
                term.id(),
                false,
                LocalDateTime.now()
        );
    }
}
