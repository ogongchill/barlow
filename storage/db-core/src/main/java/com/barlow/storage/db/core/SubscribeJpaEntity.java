package com.barlow.storage.db.core;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscribe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscribe")
public class SubscribeJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_no")
    private Long no;

    @Column(name = "subscribe_legislation_account_no", nullable = false)
    private Long subscribeLegislationAccountNo;

    @Column(name = "subscriber_no", nullable = false)
    private Long memberNo;

    protected SubscribeJpaEntity() {
    }

    SubscribeJpaEntity(Long subscribeLegislationAccountNo, Long memberNo) {
        this.subscribeLegislationAccountNo = subscribeLegislationAccountNo;
        this.memberNo = memberNo;
    }

    Subscribe toSubscribe(User user) {
        return new Subscribe(user, subscribeLegislationAccountNo, true);
    }
}
