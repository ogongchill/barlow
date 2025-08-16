package com.barlow.app.batch.recentbill.job.step;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import com.barlow.app.batch.summarization.common.BillAiSummaryEntity;
import com.barlow.app.batch.summarization.common.RecentBillJobSummaryRepository;
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

import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

@Component
@StepScope
public class TodayBillInfoWriteTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private static final String BILL_POST_TABLE_NAME = "bill_post";

	private final SimpleJdbcInsert simpleJdbcInsert;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;
	private final RecentBillJobSummaryRepository summaryRepository;

	public TodayBillInfoWriteTasklet(
		@Qualifier("batchCoreDataSource") DataSource dataSource,
		RecentBillJobScopeShareRepository jobScopeShareRepository,
		RecentBillJobSummaryRepository summaryRepository
	) {
		super();
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(BILL_POST_TABLE_NAME);
		this.jobScopeShareRepository = jobScopeShareRepository;
		this.summaryRepository = summaryRepository;
	}

	@Override
	public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY);
		TodayBillInfoBatchEntity todayBillInfo = jobScopeShareRepository.findByKey(hashKey);
		String billSummaryHashKey = super.getDataFromJobExecutionContext(RecentBillConstant.BILL_AI_SUMMARY_SHARE_KEY);
		BillAiSummaryEntity summaryEntity = summaryRepository.findByKey(billSummaryHashKey);

		saveReceivedAllInBatch(todayBillInfo.filterReceivedBills(), summaryEntity);
		saveChairmanAllInBatch(todayBillInfo.filterChairmanBills(), summaryEntity);

		return RepeatStatus.FINISHED;
	}

	private void saveReceivedAllInBatch(TodayBillInfoBatchEntity result, BillAiSummaryEntity summaryResult) {
		SqlParameterSource[] sqlParameterSources = result.items()
			.stream()
			.map(item -> createReceiveSqlParameterSource(item, summaryResult.getTextById(item.billId())))
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createReceiveSqlParameterSource(TodayBillInfoBatchEntity.BillInfoItem item, String summary) {
		return new MapSqlParameterSource()
			.addValue("bill_id", item.billId())
			.addValue("bill_name", item.billName())
			.addValue("proposers", item.proposers())
			.addValue("proposer_type", item.proposerType())
			.addValue("legislation_type", LegislationType.EMPTY)
			.addValue("progress_status", ProgressStatus.RECEIVED)
			.addValue("summary", summary)
			.addValue("detail", item.summary())
			.addValue("view_count", 0)
			.addValue("created_at", LocalDate.parse(item.proposeDateStr()), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}

	private void saveChairmanAllInBatch(TodayBillInfoBatchEntity result, BillAiSummaryEntity summaryResult) {
		SqlParameterSource[] sqlParameterSources = result.items()
			.stream()
			.map(item -> createChairmanSqlParameterSource(item, summaryResult.getTextById(item.billId())))
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createChairmanSqlParameterSource(TodayBillInfoBatchEntity.BillInfoItem item, String summary) {
		return new MapSqlParameterSource()
			.addValue("bill_id", item.billId())
			.addValue("bill_name", item.billName())
			.addValue("proposers", item.proposers())
			.addValue("proposer_type", item.proposerType())
			.addValue("legislation_type", LegislationType.findByChairman(item.proposers()))
			.addValue("progress_status", item.progressStatus())
			.addValue("summary", summary)
			.addValue("detail", null)
			.addValue("view_count", 0)
			.addValue("created_at", LocalDate.parse(item.proposeDateStr()), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
