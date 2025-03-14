package com.barlow.storage.db.core;

import java.time.LocalDateTime;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.home.todaybill.TodayBillPostThumbnail;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bill_post")
public class BillPostJpaEntity extends BaseTimeJpaEntity {

	@Id
	@Column(name = "bill_id", nullable = false, length = 100)
	private String billId;

	@Column(name = "bill_name", nullable = false)
	private String billName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(20)", name = "proposer_type", nullable = false)
	private ProposerType proposerType;

	@Column(name = "proposers", nullable = false, length = 50)
	private String proposers;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50)", name = "progress_status", nullable = false)
	private ProgressStatus progressStatus;

	@Column(columnDefinition = "text", name = "summary")
	private String summary;

	@Column(columnDefinition = "text", name = "detail")
	private String detail;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;

	@Embedded
	private PreAnnouncementInfo preAnnouncementInfo;

	protected BillPostJpaEntity() {
	}

	BillPost toBillPost() {
		BillPost billPost = new BillPost(
			new BillPost.BillInfo(billId, billName),
			new BillPost.ProposerInfo(proposerType, proposers),
			new BillPost.LegislationInfo(legislationType, progressStatus),
			summary, detail, getCreatedAt(), viewCount
		);
		if (hasPreAnnouncementInfo()) {
			billPost.assignPreAnnouncementInfo(getPreAnnouncementInfo());
		}
		return billPost;
	}

	private boolean hasPreAnnouncementInfo() {
		return preAnnouncementInfo != null;
	}

	private BillPost.PreAnnouncementInfo getPreAnnouncementInfo() {
		return new BillPost.PreAnnouncementInfo(
			preAnnouncementInfo.linkUrl,
			preAnnouncementInfo.deadlineDate.toLocalDate()
		);
	}

	TodayBillPostThumbnail toTodayBillPostThumbnail() {
		return new TodayBillPostThumbnail(
			billId,
			billName,
			proposers,
			getCreatedAt().toLocalDate()
		);
	}

	public String getBillId() {
		return billId;
	}

	public String getBillName() {
		return billName;
	}

	public ProgressStatus getProgressStatus() {
		return progressStatus;
	}

	public LegislationType getLegislationType() {
		return legislationType;
	}

	@Embeddable
	static class PreAnnouncementInfo {

		@Column(name = "pre_announce_deadline_date")
		private LocalDateTime deadlineDate;

		@Column(name = "pre_announce_link_url")
		private String linkUrl;

		protected PreAnnouncementInfo() {
		}
	}
}
