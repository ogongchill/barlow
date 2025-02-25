package com.barlow.batch.core.tracebill.job.step;

import static com.barlow.notification.NotificationRequest.BillInfo;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.tracebill.job.UpdatedBillShareRepository;
import com.barlow.batch.core.tracebill.job.UpdatedBills;
import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.notification.DefaultBillNotificationRequest;
import com.barlow.notification.NotificationSendPort;

@Component
@StepScope
public class TraceBillNotifyTasklet implements Tasklet {

	private final NotificationSendPort notificationSendPort;
	private final UpdatedBillShareRepository billShareRepository;

	public TraceBillNotifyTasklet(
		NotificationSendPort notificationSendPort,
		UpdatedBillShareRepository billShareRepository
	) {
		this.notificationSendPort = notificationSendPort;
		this.billShareRepository = billShareRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		Object hashKey = contribution.getStepExecution().getJobExecution().getExecutionContext().get("updatedBills");
		UpdatedBills updatedBills = billShareRepository.findByKey((String)hashKey);

		if (!updatedBills.isEmpty()) {
			Map<NotificationTopic, List<BillInfo>> notificationTopicMap = new EnumMap<>(NotificationTopic.class);

			notificationTopicMap.putAll(updatedBills.filterCommitteeReceived().groupByCommitteeNotificationTopic());
			notificationTopicMap.putAll(updatedBills.filterNonCommitteeReceived().groupByNonCommitteeNotificationTopic());

			notificationSendPort.sendCall(DefaultBillNotificationRequest.from(notificationTopicMap));
		}

		return RepeatStatus.FINISHED;
	}
}
