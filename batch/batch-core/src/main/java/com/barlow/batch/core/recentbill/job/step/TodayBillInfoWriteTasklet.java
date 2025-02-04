package com.barlow.batch.core.recentbill.job.step;

import java.sql.Types;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.client.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.TodayBillInfoJobParameter;
import com.barlow.storage.db.core.LegislationType;
import com.barlow.storage.db.core.ProgressStatus;
import com.barlow.storage.db.core.ProposerType;

@Component
@StepScope
public class TodayBillInfoWriteTasklet implements Tasklet {

	private static final String RECENT_BILL_POST_TABLE_NAME = "recent_bill_post";

	private final TodayBillInfoResult todayBillInfo;
	private final SimpleJdbcInsert simpleJdbcInsert;

	public TodayBillInfoWriteTasklet(
		@Value("#{jobParameters['todayBillInfo']}") String todayBillInfoStr,
		DataSource dataSource
	) {
		this.todayBillInfo = TodayBillInfoJobParameter.fromString(todayBillInfoStr);
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(RECENT_BILL_POST_TABLE_NAME);
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		saveAllInBatch(todayBillInfo);
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
			.addValue("legislation_type", LegislationType.EMPTY.name())
			.addValue("progress_status", ProgressStatus.RECEIVED.name())
			.addValue("summary", null)
			.addValue("detail", item.summary())
			.addValue("view_count", 0)
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
