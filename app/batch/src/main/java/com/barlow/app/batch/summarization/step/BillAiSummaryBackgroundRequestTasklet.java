package com.barlow.app.batch.summarization.step;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.summarization.BackgroundRequestStatusEntity;
import com.barlow.app.batch.summarization.BackgroundRequestStatusRepository;
import com.barlow.app.batch.summarization.common.OpenAiRequestExecutor;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.app.batch.utils.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BillAiSummaryBackgroundRequestTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(BillAiSummaryBackgroundRequestTasklet.class);
    private final OpenAiRequestExecutor executor;
    private final RecentBillJobScopeShareRepository recentBillJobScopeShareRepository;
    private final BackgroundRequestStatusRepository backgroundRequestStatusRepository;

    public BillAiSummaryBackgroundRequestTasklet(
            OpenAiRequestExecutor executor,
            RecentBillJobScopeShareRepository recentBillJobScopeShareRepository,
            BackgroundRequestStatusRepository backgroundRequestStatusRepository
    ) {
        this.executor = executor;
        this.recentBillJobScopeShareRepository = recentBillJobScopeShareRepository;
        this.backgroundRequestStatusRepository = backgroundRequestStatusRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
        TodayBillInfoBatchEntity todayBillInfoBatchEntity = getTodayBillInfoBatchEntity(jobExecution);

        List<SummaryRequestStatus> statuses = executor.requestAsync(todayBillInfoBatchEntity);
        BackgroundRequestStatusEntity entity = new BackgroundRequestStatusEntity(LocalDate.now(), statuses);

        putData(entity);
        log.info("{}개의 open ai background 요약 요청 완료", entity.statuses().size());
        return RepeatStatus.FINISHED;
    }

    private void putData(BackgroundRequestStatusEntity entity) {
        String backgroundRequestHashKey = HashUtil.generate(entity);
        backgroundRequestStatusRepository.save(backgroundRequestHashKey, entity);
        super.putDataToExecutionContext(RecentBillConstant.BILL_AI_BACKGROUND_REQUEST_SHARE_KEY, backgroundRequestHashKey);
    }

    private TodayBillInfoBatchEntity getTodayBillInfoBatchEntity(JobExecution jobExecution) {
        super.setCurrentExecutionContext(jobExecution.getExecutionContext());
        String recentBillHashKey = super.getDataFromJobExecutionContext(RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY);
        return recentBillJobScopeShareRepository.findByKey(recentBillHashKey);
    }
}
