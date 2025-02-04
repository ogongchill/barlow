package com.barlow.storage.db.core;

import com.barlow.core.domain.recentbill.RecentBillPost;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recent_bill_post")
public class RecentBillPostJpaEntity extends BaseTimeJpaEntity {

	@Id
	@Column(name = "bill_id", nullable = false, length = 100)
	private String billId;

	@Column(name = "bill_name", nullable = false)
	private String billName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar", name = "proposer_type", nullable = false)
	private ProposerType proposerType;

	@Column(name = "proposers", nullable = false, length = 50)
	private String proposers;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar", name = "progress_status", nullable = false)
	private ProgressStatus progressStatus;

	@Column(columnDefinition = "text", name = "summary")
	private String summary;

	@Column(columnDefinition = "text", name = "detail")
	private String detail;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;

	protected RecentBillPostJpaEntity() {
	}

	RecentBillPost toRecentBillPost() {
		return new RecentBillPost(
			new RecentBillPost.BillInfo(billId, billName),
			new RecentBillPost.ProposerInfo(proposerType.getValue(), proposers),
			new RecentBillPost.LegislationInfo(legislationType.getValue(), progressStatus.getValue()),
			summary, detail, getCreatedAt(), viewCount
		);
	}
}
