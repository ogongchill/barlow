package com.barlow.batch.core.recentbill.job.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.client.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.TodayBillInfoJobParameter;
import com.barlow.notification.DefaultBillNotificationRequest;
import com.barlow.notification.NotificationRequest;
import com.barlow.notification.NotificationSendPort;

@Component
@StepScope
public class TodayBillNotifierTasklet implements Tasklet {

	private static final String DEFAULT_BILL_STATUS = "접수";

	private final TodayBillInfoResult todayBillInfo;
	private final NotificationSendPort notificationSendPort;

	public TodayBillNotifierTasklet(
		@Value("#{jobParameters['todayBillInfo']}") String todayBillInfoStr,
		NotificationSendPort notificationSendPort
	) {
		this.todayBillInfo = TodayBillInfoJobParameter.fromString(todayBillInfoStr);
		this.notificationSendPort = notificationSendPort;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		DefaultBillNotificationRequest notificationRequest = DefaultBillNotificationRequest.of(
			DEFAULT_BILL_STATUS,
			todayBillInfo.items()
				.stream()
				.map(info -> new NotificationRequest.BillInfo(info.billId(), info.billName()))
				.toList()
		);
		notificationSendPort.sendCall(notificationRequest);
		return RepeatStatus.FINISHED;
	}
}
