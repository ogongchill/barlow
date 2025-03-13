package com.barlow.batch.core.preannounce.job.listener;

import static com.barlow.batch.core.preannounce.PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.batch.core.preannounce.job.PreAnnounceRetrieveClient;
import com.barlow.batch.core.utils.HashUtil;

@Component
public class PreAnnounceBillDirtyCheckJobListener
	extends AbstractExecutionContextSharingManager
	implements JobExecutionListener {

	private final PreAnnounceRetrieveClient client;
	private final PreAnnounceBillShareRepository shareRepository;

	public PreAnnounceBillDirtyCheckJobListener(
		PreAnnounceRetrieveClient client,
		PreAnnounceBillShareRepository shareRepository
	) {
		super();
		this.client = client;
		this.shareRepository = shareRepository;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		CurrentPreAnnounceBills currentPreAnnounceBills = client.getPreAnnouncement();

		String hashKey = HashUtil.generate(currentPreAnnounceBills);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(NEW_PRE_ANNOUNCE_BILL_SHARE_KEY, hashKey);
		shareRepository.save(hashKey, currentPreAnnounceBills);
	}
}
