package com.barlow.batch.core.recentbill.job.step;

import java.sql.Types;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.jetbrains.annotations.NotNull;
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

import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

@Component
@StepScope
public class TodayBillInfoWriteTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private static final String BILL_POST_TABLE_NAME = "bill_post";

	private final SimpleJdbcInsert simpleJdbcInsert;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public TodayBillInfoWriteTasklet(
		@Qualifier("coreDataSource") DataSource dataSource,
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(BILL_POST_TABLE_NAME);
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TODAY_BILL_INFO_JOB_KEY);
		saveAllInBatch(jobScopeShareRepository.findByKey(hashKey));
		return RepeatStatus.FINISHED;
	}

	private void saveAllInBatch(TodayBillInfoResult result) {
		SqlParameterSource[] sqlParameterSources = result.items()
			.stream()
			.map(this::createSqlParameterSource)
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createSqlParameterSource(TodayBillInfoResult.BillInfoItem item) {
		return new MapSqlParameterSource()
			.addValue("bill_id", item.billId())
			.addValue("bill_name", item.billName())
			.addValue("proposers", item.proposers())
			.addValue("proposer_type", ProposerType.findByValue(item.proposerType()))
			.addValue("legislation_type", LegislationType.EMPTY)
			.addValue("progress_status", ProgressStatus.RECEIVED)
			.addValue("summary", null)
			.addValue("detail", item.summary())
			.addValue("view_count", 0)
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
