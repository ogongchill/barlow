package com.barlow.app.batch.preannounce.job.listener;

import static com.barlow.app.batch.preannounce.PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.app.batch.preannounce.job.PreAnnounceBillShareRepository;
import com.barlow.app.batch.preannounce.job.PreAnnounceRetrieveClient;
import com.barlow.app.batch.utils.HashUtil;

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
