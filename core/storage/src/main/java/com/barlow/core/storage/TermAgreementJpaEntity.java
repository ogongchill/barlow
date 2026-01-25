package com.barlow.core.storage;

import jakarta.persistence.*;

import java.time.LocalDate;

public class TermAgreementJpaEntity extends BaseTimeJpaEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_agreement_no")
    private Long no;

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "term_id", nullable = false)
    private Long termId;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreed_at", nullable = false)
    private LocalDate agreedAt;
}
