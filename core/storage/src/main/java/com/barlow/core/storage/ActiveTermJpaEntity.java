package com.barlow.core.storage;

import com.barlow.core.domain.registration.Term;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;


public class ActiveTermJpaEntity extends BaseTimeJpaEntity {

    @Id
    @Enumerated(value = EnumType.STRING)
    private Term.Type termType;

    @Column(name = "term_id", nullable = false)
    private Long termId;
}
