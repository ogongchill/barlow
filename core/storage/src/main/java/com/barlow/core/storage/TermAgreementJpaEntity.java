package com.barlow.core.storage;

import com.barlow.core.domain.registration.Term;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class TermAgreementJpaEntity extends BaseTimeJpaEntity{

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "term_id", nullable = false)
    private Long termId;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreed_at", nullable = false)
    private LocalDate agreedAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "term_type", nullable = false)
    private Term.Type termType;
}
