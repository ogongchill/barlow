package com.barlow.storage.db.core;

import java.time.LocalDateTime;

import com.barlow.core.enumerate.LegislationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pre_announcement_bill")
public class PreAnnounceBillJpaEntity extends BaseTimeJpaEntity {

	@Id
	@Column(name = "bill_id", nullable = false, length = 100)
	private String billId;

	@Column(name = "bill_name", nullable = false)
	private String billName;

	@Column(name = "proposers", nullable = false, length = 50)
	private String proposers;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	@Column(name = "deadline_dater", nullable = false)
	private LocalDateTime deadlineDate;

	@Column(name = "link_url", nullable = false)
	private String linkUrl;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "progress_status", nullable = false)
	private ProgressStatus progressStatus;

	protected PreAnnounceBillJpaEntity() {
	}

	public String getBillId() {
		return billId;
	}

	public enum ProgressStatus {
		IN_PROGRESS, FINISHED
	}
}
