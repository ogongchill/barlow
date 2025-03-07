package com.barlow.batch.core.preannounce.job.listener;

import static com.barlow.batch.core.preannounce.PreAnnounceConstant.BATCH_DATE_JOB_PARAMETER;
import static com.barlow.batch.core.preannounce.PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillBatchRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceRetrieveClient;
import com.barlow.batch.core.preannounce.job.PreviousPreAnnounceBillIds;
import com.barlow.batch.core.utils.HashUtil;

@Component
public class PreAnnounceBillDirtyCheckJobListener
	extends AbstractExecutionContextSharingManager
	implements JobExecutionListener {

	private final PreAnnounceRetrieveClient client;
	private final PreAnnounceBillBatchRepository preAnnounceBillBatchRepository;
	private final PreAnnounceBillShareRepository shareRepository;

	public PreAnnounceBillDirtyCheckJobListener(
		PreAnnounceRetrieveClient client,
		PreAnnounceBillBatchRepository preAnnounceBillBatchRepository,
		PreAnnounceBillShareRepository shareRepository
	) {
		this.client = client;
		this.preAnnounceBillBatchRepository = preAnnounceBillBatchRepository;
		this.shareRepository = shareRepository;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LocalDate batchDate = jobExecution.getJobParameters().getLocalDate(BATCH_DATE_JOB_PARAMETER);
		CurrentPreAnnounceBills currentPreAnnounceBills = client.getPreAnnouncement();
		PreviousPreAnnounceBillIds previousPreAnnounceBillIds = preAnnounceBillBatchRepository
			.retrieveAllInProgress(batchDate);
		NewPreAnnounceBills newPreAnnounceBills = currentPreAnnounceBills.dirtyCheck(previousPreAnnounceBillIds);

		String hashKey = HashUtil.generate(newPreAnnounceBills);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(NEW_PRE_ANNOUNCE_BILL_SHARE_KEY, hashKey);
		shareRepository.save(hashKey, newPreAnnounceBills);
	}
}
