package com.barlow.storage.db.core;

import com.barlow.core.domain.home.MyLegislationAccount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "legislation_account")
public class LegislationAccountJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "legislation_account_no")
	private Long no;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	@Column(name = "description", nullable = false, length = 500)
	private String description;

	@Column(name = "post_count", nullable = false)
	private Integer postCount;

	@Column(name = "subscriber_count", nullable = false)
	private Integer subscriberCount;

	protected LegislationAccountJpaEntity() {
	}

	MyLegislationAccount toMyLegislationAccount() {
		return new MyLegislationAccount(
			no,
			legislationType.getValue(),
			legislationType.getIconUrl(),
			description,
			postCount,
			subscriberCount
		);
	}
}
