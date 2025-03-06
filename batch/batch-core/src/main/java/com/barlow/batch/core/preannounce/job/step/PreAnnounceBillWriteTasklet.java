package com.barlow.batch.core.preannounce.job.step;

import java.sql.Types;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.preannounce.PreAnnounceConstant;
import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBatchEntity;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillShareRepository;

@Component
@StepScope
public class PreAnnounceBillWriteTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private static final String PRE_ANNOUNCE_BILL_TABLE_NAME = "pre_announcement_bill";

	private final SimpleJdbcInsert simpleJdbcInsert;
	private final PreAnnounceBillShareRepository shareRepository;

	public PreAnnounceBillWriteTasklet(
		@Qualifier("coreDataSource") DataSource dataSource,
		PreAnnounceBillShareRepository shareRepository
	) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(PRE_ANNOUNCE_BILL_TABLE_NAME);
		this.shareRepository = shareRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(PreAnnounceConstant.NEW_PRE_ANNOUNCE_BILL_SHARE_KEY);
		NewPreAnnounceBills newPreAnnounceBills = shareRepository.findByKey(hashKey);

		saveAllInBatch(newPreAnnounceBills);

		return RepeatStatus.FINISHED;
	}

	private void saveAllInBatch(NewPreAnnounceBills newPreAnnounceBills) {
		SqlParameterSource[] sqlParameterSources = newPreAnnounceBills.values()
			.stream()
			.map(this::createSqlParameterSource)
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createSqlParameterSource(PreAnnounceBatchEntity batchEntity) {
		return new MapSqlParameterSource()
			.addValue("bill_id", batchEntity.billId())
			.addValue("bill_name", batchEntity.billName())
			.addValue("proposers", batchEntity.proposers())
			.addValue("legislation_type", batchEntity.legislationType())
			.addValue("deadline_dater", batchEntity.deadlineDate())
			.addValue("link_url", batchEntity.linkUrl())
			.addValue("progress_status", "IN_PROGRESS")
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
