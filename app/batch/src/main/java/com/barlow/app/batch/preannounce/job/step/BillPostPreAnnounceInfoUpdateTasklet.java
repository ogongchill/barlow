package com.barlow.app.batch.preannounce.job.step;

import static com.barlow.app.batch.preannounce.PreAnnounceConstant.*;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.preannounce.PreAnnounceConstant;
import com.barlow.app.batch.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.app.batch.preannounce.job.NewPreAnnounceBills;
import com.barlow.app.batch.preannounce.job.PreAnnounceBillPostBatchRepository;
import com.barlow.app.batch.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.app.batch.preannounce.job.PreviousPreAnnounceBillIds;

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
