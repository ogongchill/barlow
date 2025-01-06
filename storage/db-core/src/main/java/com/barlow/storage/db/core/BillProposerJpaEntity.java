package com.barlow.storage.db.core;

import com.barlow.core.domain.recentbill.BillProposer;

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

	@Column(name = "proposer_no", nullable = false)
	private Long proposerNo;

	@Column(name = "proposer_name", nullable = false)
	private String proposerName;

	@Column(name = "proposer_profile_image_path", nullable = false)
	private String proposerProfileImagePath;

	@Enumerated(EnumType.STRING)
	@Column(name = "party_name", nullable = false)
	private PartyName partyName;

	protected BillProposerJpaEntity() {
	}

	BillProposer toBillProposer() {
		return new BillProposer(
			proposerNo,
			proposerName,
			partyName.getValue(),
			proposerProfileImagePath
		);
	}
}
