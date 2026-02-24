package com.barlow.app.api.controller.v1.auth;

import com.barlow.core.domain.account.term.TermAgreement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record TermAgreementRequest(
    Map<Long, Boolean> termAgreements
) {
    public List<TermAgreement> toTermAgreements(LocalDateTime submittedAt) {
        return termAgreements.entrySet()
                .stream()
                .map(entry -> Boolean.TRUE.equals(entry.getValue())
                        ? TermAgreement.agreedAt(entry.getKey(), submittedAt)
                        : TermAgreement.disagreedAt(entry.getKey(), submittedAt))
                .toList();
    }
}
