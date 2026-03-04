package com.barlow.batch.admin.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.batch.admin.bill.job.BillBatchJobExecutor;

@RestController
@RequestMapping("/admin/v1")
public class AdminBatchController {

	private final BillBatchJobExecutor billBatchJobExecutor;

	public AdminBatchController(BillBatchJobExecutor billBatchJobExecutor) {
		this.billBatchJobExecutor = billBatchJobExecutor;
	}

	@PostMapping("/bill-setting")
	public ResponseEntity<Void> all(
		@RequestParam(name = "startDateStr") String startDateStr,
		@RequestParam(name = "endDateStr") String endDateStr
	) {
		billBatchJobExecutor.execute(LocalDate.parse(startDateStr), LocalDate.parse(endDateStr));
		return ResponseEntity.ok().build();
	}
}
