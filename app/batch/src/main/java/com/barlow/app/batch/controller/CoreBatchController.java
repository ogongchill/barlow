package com.barlow.app.batch.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.app.batch.preannounce.job.PreAnnounceBatchJobExecutor;
import com.barlow.app.batch.recentbill.job.RecentBillBatchJobExecutor;
import com.barlow.app.batch.tracebill.job.TrackingBillBatchJobExecutor;

@RestController
@RequestMapping("/api/v1")
public class CoreBatchController {

	private final RecentBillBatchJobExecutor recentBillBatchJobExecutor;
	private final PreAnnounceBatchJobExecutor preAnnounceBatchJobExecutor;
	private final TrackingBillBatchJobExecutor trackingBillBatchJobExecutor;

	public CoreBatchController(
		RecentBillBatchJobExecutor recentBillBatchJobExecutor,
		PreAnnounceBatchJobExecutor preAnnounceBatchJobExecutor,
		TrackingBillBatchJobExecutor trackingBillBatchJobExecutor
	) {
		this.recentBillBatchJobExecutor = recentBillBatchJobExecutor;
		this.preAnnounceBatchJobExecutor = preAnnounceBatchJobExecutor;
		this.trackingBillBatchJobExecutor = trackingBillBatchJobExecutor;
	}

	@PostMapping("/pre-announcement-bill-job")
	public ResponseEntity<Void> executePreAnnounceBatchJob() {
		preAnnounceBatchJobExecutor.execute(LocalDate.now());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/tracking-bill-job")
	public ResponseEntity<Void> executeTrackingBillBatchJob(@RequestBody TrackingStartDateRequest request) {
		trackingBillBatchJobExecutor.execute(
			LocalDate.now().minusDays(1),
			LocalDate.parse(request.startDate())
		);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/today-bill-job")
	public ResponseEntity<Void> executeTodayBatchJob() {
		recentBillBatchJobExecutor.execute(LocalDate.now());
		return ResponseEntity.noContent().build();
	}
}
