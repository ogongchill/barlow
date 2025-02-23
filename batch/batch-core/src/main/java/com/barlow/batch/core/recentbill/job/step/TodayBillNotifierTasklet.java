package com.barlow.batch.core.recentbill.job.step;

import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.notification.DefaultBillNotificationRequest;
import com.barlow.notification.NotificationRequest;
import com.barlow.notification.NotificationSendPort;

@Component
@StepScope
public class TodayBillNotifierTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final NotificationSendPort notificationSendPort;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public TodayBillNotifierTasklet(
		NotificationSendPort notificationSendPort,
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.notificationSendPort = notificationSendPort;
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TODAY_BILL_INFO_JOB_KEY);
		TodayBillInfoResult todayBillInfo = jobScopeShareRepository.findByKey(hashKey);

		DefaultBillNotificationRequest notificationRequest = DefaultBillNotificationRequest.from(
			todayBillInfo.items()
				.stream()
				.collect(Collectors.groupingBy(
					TodayBillInfoResult.BillInfoItem::progressStatusCode,
					Collectors.mapping(item -> new NotificationRequest.BillInfo(
						item.billId(), item.billName()
					), Collectors.toList())
				))
		);
		notificationSendPort.sendCall(notificationRequest);
		return RepeatStatus.FINISHED;
	}
}
