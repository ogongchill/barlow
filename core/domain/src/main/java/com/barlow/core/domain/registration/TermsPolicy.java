package com.barlow.core.domain.registration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TermsPolicy {

    private final Set<Long> requiredTermIds;

    private TermsPolicy(Set<Long> requiredTermIds) {
        this.requiredTermIds = requiredTermIds;
    }

    public static TermsPolicy from(List<Term> requirements) {
        Set<Long> requiredTermIds = requirements.stream()
                .map(Term::id)
                .collect(Collectors.toSet());
        return new TermsPolicy(requiredTermIds);
    }

    public void validate(List<TermAgreement> agreements) {
        Set<Long> agreedTermIds = agreements.stream()
                .filter(TermAgreement::agreed)
                .map(TermAgreement::termId)
                .collect(Collectors.toSet());
        if (!agreedTermIds.containsAll(requiredTermIds)) {
            Set<Long> missingTermIds = requiredTermIds.stream()
                    .filter(id -> !agreedTermIds.contains(id))
                    .collect(Collectors.toSet());
            throw RegistrationException.requiredTermsNotAccepted(missingTermIds);
        }
    }
}
