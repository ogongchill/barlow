package com.barlow.core.domain.registration;

import java.util.List;

public record UserTermAgreementCommand(
        Long userNo,
        List<TermAgreement> agreements
) {
}
