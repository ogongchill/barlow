package com.barlow.batch.core.preannounce.job.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.preannounce.PreAnnounceConstant;
import com.barlow.batch.core.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillBatchRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceRetrieveClient;
import com.barlow.batch.core.preannounce.job.PreviousPreAnnounceBillIds;
import com.barlow.batch.core.utils.HashUtil;

@Component
@StepScope
public class PreAnnounceBillDirtyCheckTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final PreAnnounceRetrieveClient client;
	private final PreAnnounceBillBatchRepository preAnnounceBillBatchRepository;
	private final PreAnnounceBillShareRepository shareRepository;

	public PreAnnounceBillDirtyCheckTasklet(
		PreAnnounceRetrieveClient client,
		PreAnnounceBillBatchRepository preAnnounceBillBatchRepository,
		PreAnnounceBillShareRepository shareRepository
	) {
		this.client = client;
		this.preAnnounceBillBatchRepository = preAnnounceBillBatchRepository;
		this.shareRepository = shareRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		CurrentPreAnnounceBills currentPreAnnounceBills = client.getPreAnnouncement();
		PreviousPreAnnounceBillIds previousPreAnnounceBillIds = preAnnounceBillBatchRepository.retrieveAllInProgress();
		NewPreAnnounceBills newPreAnnounceBills = currentPreAnnounceBills.dirtyCheck(previousPreAnnounceBillIds);

		String hashKey = HashUtil.generate(newPreAnnounceBills);
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		super.putDataToExecutionContext(PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY, hashKey);
		shareRepository.save(hashKey, newPreAnnounceBills);

		return RepeatStatus.FINISHED;
	}
}
