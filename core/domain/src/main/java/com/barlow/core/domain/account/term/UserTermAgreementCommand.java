package com.barlow.core.domain.account.term;

import java.util.List;

public record UserTermAgreementCommand(
        Long userNo,
        List<TermAgreement> agreements
) {
}
