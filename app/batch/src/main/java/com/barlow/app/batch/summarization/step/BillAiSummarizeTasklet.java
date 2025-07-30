package com.barlow.app.batch.summarization.step;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.summarization.BillAiSummary;
import com.barlow.app.batch.summarization.BillAiSummaryEntity;
import com.barlow.app.batch.summarization.RecentBillJobSummaryRepository;
import com.barlow.app.batch.summarization.TextSummarizeClient;
import com.barlow.app.batch.utils.HashUtil;
import com.barlow.client.ai.openai.api.exception.OpenAiApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillAiSummarizeTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(BillAiSummarizeTasklet.class);
    private final TextSummarizeClient summarizeClient;
    private final RetryTemplate retryTemplate;
    private final RecentBillJobScopeShareRepository recentBillJobScopeShareRepository;
    private final RecentBillJobSummaryRepository recentBillJobSummaryRepository;

    public BillAiSummarizeTasklet(TextSummarizeClient summarizeClient, RecentBillJobScopeShareRepository recentBillJobScopeShareRepository, RecentBillJobSummaryRepository recentBillJobSummaryRepository) {
        this.summarizeClient = summarizeClient;
        this.recentBillJobScopeShareRepository = recentBillJobScopeShareRepository;
        this.recentBillJobSummaryRepository = recentBillJobSummaryRepository;
        this.retryTemplate = RetryTemplate.builder()
                .exponentialBackoff(1000, 2, 10000)
                .retryOn(OpenAiApiException.class)
                .withListener(new RetryListener() {
                    @Override
                    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                        log.warn("법안 원문 요약 재시도 실패: {} ({}회)", throwable.getMessage(), context.getRetryCount());
                    }
                })
                .maxAttempts(3)
                .build();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
        TodayBillInfoBatchEntity todayBillInfoBatchEntity = getTodayBillInfoBatchEntity(jobExecution);

        List<BillAiSummary> billAiSummaries = requestAiSummary(todayBillInfoBatchEntity);

        BillAiSummaryEntity billSummaryEntity = new BillAiSummaryEntity(billAiSummaries);
        putSummaryResult(billSummaryEntity);

        return RepeatStatus.FINISHED;
    }

    private void putSummaryResult(BillAiSummaryEntity billSummaryEntity) {
        String billSummaryHashKey = HashUtil.generate(billSummaryEntity);
        recentBillJobSummaryRepository.save(billSummaryHashKey, billSummaryEntity);
        super.putDataToExecutionContext(RecentBillConstant.BILL_AI_SUMMARY_SHARE_KEY, billSummaryHashKey);
    }

    private TodayBillInfoBatchEntity getTodayBillInfoBatchEntity(JobExecution jobExecution) {
        super.setCurrentExecutionContext(jobExecution.getExecutionContext());
        String recentBillHashKey = super.getDataFromJobExecutionContext(RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY);
        return recentBillJobScopeShareRepository.findByKey(recentBillHashKey);
    }

    private List<BillAiSummary> requestAiSummary(TodayBillInfoBatchEntity todayBillInfoBatchEntity) {
        List<BillAiSummary> billSummarizeTexts = new ArrayList<>();
        for (TodayBillInfoBatchEntity.BillInfoItem item : todayBillInfoBatchEntity.items()) {
            String result = retryTemplate.execute(ctx -> summarizeClient.summarize(item.summary()));
            billSummarizeTexts.add(new BillAiSummary(item.billId(), result));
        }
        return billSummarizeTexts;
    }
}
