package com.barlow.batch.core.preannounce.job.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.preannounce.PreAnnounceConstant;
import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillPostBatchRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;

@Component
@StepScope
public class BillPostPreAnnounceInfoUpdateTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final PreAnnounceBillShareRepository shareRepository;
	private final PreAnnounceBillPostBatchRepository batchRepository;

	public BillPostPreAnnounceInfoUpdateTasklet(
		PreAnnounceBillShareRepository shareRepository,
		PreAnnounceBillPostBatchRepository batchRepository
	) {
		this.shareRepository = shareRepository;
		this.batchRepository = batchRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY);
		NewPreAnnounceBills newPreAnnounceBills = shareRepository.findByKey(hashKey);

		batchRepository.updateBillPostPreAnnounceInfo(newPreAnnounceBills);

		return RepeatStatus.FINISHED;
	}
}
