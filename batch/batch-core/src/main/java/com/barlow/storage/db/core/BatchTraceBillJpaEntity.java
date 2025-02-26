package com.barlow.storage.db.core;

import com.barlow.batch.core.tracebill.job.PreviousBillBatchEntity;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "batch_trace_bill")
public class BatchTraceBillJpaEntity extends BaseTimeJpaEntity {

	@Id
	@Column(name = "bill_id", nullable = false, length = 100)
	private String billId;

	@Column(name = "bill_name", nullable = false)
	private String billName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50)", name = "progress_status", nullable = false)
	private ProgressStatus progressStatus;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "legislation_type", nullable = false)
	private LegislationType legislationType;

	protected BatchTraceBillJpaEntity() {
	}

	PreviousBillBatchEntity toPreviousBillBatchEntity() {
		return new PreviousBillBatchEntity(billId, billName, progressStatus, legislationType);
	}
}
