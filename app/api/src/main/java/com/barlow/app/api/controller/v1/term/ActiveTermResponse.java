package com.barlow.app.api.controller.v1.term;

import com.barlow.core.domain.account.term.Term;

import java.time.LocalDateTime;
import java.util.List;

public record ActiveTermResponse(
        List<TermInfo> activeTerms
) {
    public record TermInfo(
            String title,
            String linkUrl,
            String type,
            String version,
            boolean required,
            LocalDateTime effectiveAt
    ) {}

    public static ActiveTermResponse fromTerms(List<Term> activeTerms) {
        List<TermInfo> terms = activeTerms.stream()
                .map(term -> new TermInfo(
                        term.title(),
                        term.linkUrl(),
                        term.termType().name(),
                        term.version(),
                        term.required(),
                        term.effectiveAt()
                    )).toList();
        return new ActiveTermResponse(terms);
    }
}
