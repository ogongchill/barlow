package com.barlow.app.scheduler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BatchLambdaScheduler {

	private final RestTemplate restTemplate;
	private final String preAnnounceBatchFuncUrl;
	private final String trackingBillBatchFuncUrl;
	private final String updateBillBatchFuncUrl;

	public BatchLambdaScheduler(
		@Value("${scheduler.batch-lambda.url.pre-announce}") String preAnnounceBatchFuncUrl,
		@Value("${scheduler.batch-lambda.url.tracking-bill}") String trackingBillBatchFuncUrl,
		@Value("${scheduler.batch-lambda.url.today-bill}") String updateBillBatchFuncUrl,
		RestTemplate restTemplate
	) {
		this.preAnnounceBatchFuncUrl = preAnnounceBatchFuncUrl;
		this.trackingBillBatchFuncUrl = trackingBillBatchFuncUrl;
		this.updateBillBatchFuncUrl = updateBillBatchFuncUrl;
		this.restTemplate = restTemplate;
	}

	@Scheduled(cron = "0 0 12 * * 1-5")
	public void scheduledTrackingPreAnnouncementBillBatch() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		restTemplate.exchange(preAnnounceBatchFuncUrl, HttpMethod.POST, new HttpEntity<>(headers), Void.class);
	}

	@Scheduled(cron = "0 30 15 * * 1-5")
	public void scheduledTrackingBillInfoBatch() {
		Map<String, Object> body = new HashMap<>();
		body.put("startDate", LocalDate.now().minusYears(1).withDayOfMonth(1).toString());
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		restTemplate.exchange(trackingBillBatchFuncUrl, HttpMethod.POST, new HttpEntity<>(body, headers), Void.class);
	}

	@Scheduled(cron = "0 0 19 * * 1-5")
	public void scheduledUpdateTodayBillBatch() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		restTemplate.exchange(updateBillBatchFuncUrl, HttpMethod.POST, new HttpEntity<>(headers), Void.class);
	}
}
