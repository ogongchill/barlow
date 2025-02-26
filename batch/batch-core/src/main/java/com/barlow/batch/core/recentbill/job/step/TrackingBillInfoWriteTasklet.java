package com.barlow.batch.core.recentbill.job.step;

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
import com.barlow.batch.core.recentbill.RecentBillConstant;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

@Component
@StepScope
public class TrackingBillInfoWriteTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private static final String BATCH_TRACE_BILL_TABLE_NAME = "batch_trace_bill";

	private final SimpleJdbcInsert simpleJdbcInsert;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public TrackingBillInfoWriteTasklet(
		@Qualifier("coreDataSource") DataSource dataSource,
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(BATCH_TRACE_BILL_TABLE_NAME);
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY);
		TodayBillInfoResult todayBillInfo = jobScopeShareRepository.findByKey(hashKey);

		saveReceivedAllInBatch(todayBillInfo.filterReceivedBills());

		return RepeatStatus.FINISHED;
	}

	private void saveReceivedAllInBatch(TodayBillInfoResult result) {
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
			.addValue("legislation_type", LegislationType.EMPTY)
			.addValue("progress_status", ProgressStatus.RECEIVED)
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
