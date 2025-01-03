package com.barlow.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bill_proposer")
public class BillProposerJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_proposer_no")
	private Long no;

	@Column(name = "propose_bill_id", nullable = false)
	private String proposeBillId;

	@Column(name = "lawmaker_no", nullable = false)
	private Long lawmakerNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "party_name", nullable = false)
	private PartyName partyName;

	protected BillProposerJpaEntity() {
	}
}
