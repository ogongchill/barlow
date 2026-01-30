package com.barlow.core.domain.registration;

import java.time.LocalDateTime;

public record TermAgreement(
        Long termId,
        boolean agreed,
        LocalDateTime agreedAt
) {

    public static TermAgreement agreedAt(Long termId, LocalDateTime agreedAt) {
        return new TermAgreement(termId, true, agreedAt);
    }

    public static TermAgreement disagreedAt(Long termId, LocalDateTime agreedAt) {
        return new TermAgreement(termId, false, agreedAt);
    }
}
