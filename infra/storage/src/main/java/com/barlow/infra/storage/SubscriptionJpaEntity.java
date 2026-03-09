package com.barlow.infra.storage;

import com.barlow.core.domain.subscribe.Subscription;
import com.barlow.core.enumerate.LegislationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscribe")
public class SubscriptionJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscribe_no")
	private Long no;

	@Column(name = "subscribe_legislation_account_no", nullable = false)
	private Long subscribeLegislationAccountNo;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	@Column(name = "subscriber_no", nullable = false)
	private Long memberNo;

	protected SubscriptionJpaEntity() {}

	SubscriptionJpaEntity(Long subscribeLegislationAccountNo, LegislationType legislationType, Long memberNo) {
		this.subscribeLegislationAccountNo = subscribeLegislationAccountNo;
		this.legislationType = legislationType;
		this.memberNo = memberNo;
	}

	Subscription toSubscription() {
		return new Subscription(memberNo, subscribeLegislationAccountNo, legislationType, true);
	}

	LegislationType getLegislationType() {
		return legislationType;
	}
}
