package com.barlow.core.storage;

import com.barlow.core.domain.registration.TermAgreement;
import com.barlow.core.domain.registration.UserTermAgreementCommand;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "term_agreement")
public class TermAgreementJpaEntity extends BaseTimeJpaEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_agreement_no")
    private Long no;

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "term_no", nullable = false)
    private Long termNo;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreed_at", nullable = false)
    private LocalDateTime agreedAt;

    public TermAgreementJpaEntity(
            Long memberNo,
            Long termNo,
            boolean agreed,
            LocalDateTime agreedAt
    ) {
        this.memberNo = memberNo;
        this.termNo = termNo;
        this.agreed = agreed;
        this.agreedAt = agreedAt;
    }

    public TermAgreementJpaEntity() {
    }

    public static List<TermAgreementJpaEntity> fromCommand(UserTermAgreementCommand command) {
        return command.agreements()
                .stream()
                .map(agreement -> new TermAgreementJpaEntity(
                        command.userNo(),
                        agreement.termId(),
                        agreement.agreed(),
                        agreement.agreedAt()
                )).toList();
    }

    public TermAgreement toTermAgreement() {
        return new TermAgreement(termNo, agreed, agreedAt);
    }
}
