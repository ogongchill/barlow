package com.barlow.batch.core.preannounce.job.step;

import static com.barlow.batch.core.preannounce.PreAnnounceConstant.*;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
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
import com.barlow.batch.core.preannounce.job.PreAnnounceBillPostBatchRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.batch.core.preannounce.job.PreviousPreAnnounceBillIds;

@Component
@StepScope
public class BillPostPreAnnounceInfoUpdateTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final PreAnnounceBillShareRepository shareRepository;
	private final PreAnnounceBillPostBatchRepository batchRepository;

	public BillPostPreAnnounceInfoUpdateTasklet(
		PreAnnounceBillShareRepository shareRepository,
		PreAnnounceBillPostBatchRepository batchRepository
	) {
		super();
		this.shareRepository = shareRepository;
		this.batchRepository = batchRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY);
		CurrentPreAnnounceBills currentPreAnnounceBills = shareRepository.findByKey(hashKey);

		LocalDate batchDate = jobExecution.getJobParameters().getLocalDate(BATCH_DATE_JOB_PARAMETER);
		PreviousPreAnnounceBillIds previousPreAnnounceBillIds = batchRepository.retrieveAllInProgress(batchDate);
		NewPreAnnounceBills newPreAnnounceBills = currentPreAnnounceBills.dirtyCheck(previousPreAnnounceBillIds);
		batchRepository.updateBillPostPreAnnounceInfo(newPreAnnounceBills);

		return RepeatStatus.FINISHED;
	}
}
