package com.barlow.app.batch.summarization.step.poll;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.summarization.common.*;
import com.barlow.app.batch.utils.HashUtil;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class BillAiSummaryPollTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

    private final BackgroundRequestStatusRepository backgroundRequestStatusRepository;
    private final RecentBillJobSummaryRepository recentBillJobSummaryRepository;
    private final BillAiSummaryPollExecutor executor;

    public BillAiSummaryPollTasklet(BackgroundRequestStatusRepository backgroundRequestStatusRepository, RecentBillJobSummaryRepository recentBillJobSummaryRepository, BillAiSummaryPollExecutor executor) {
        this.backgroundRequestStatusRepository = backgroundRequestStatusRepository;
        this.recentBillJobSummaryRepository = recentBillJobSummaryRepository;
        this.executor = executor;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
        BackgroundRequestStatusEntity backgroundRequestStatusEntity = getBackgroundRequestStatusEntity(jobExecution);
        List<BillAiSummary> summaries = executor.requestAsync(backgroundRequestStatusEntity)
                .stream()
                .filter(Objects::nonNull)
                .toList();
        BillAiSummaryEntity billAiSummaryEntity = new BillAiSummaryEntity(summaries);
        putData(billAiSummaryEntity);
        return RepeatStatus.FINISHED;
    }

    private void putData(BillAiSummaryEntity billAiSummaryEntity) {
        String hashKey = HashUtil.generate(billAiSummaryEntity);
        super.putDataToExecutionContext(RecentBillConstant.BILL_AI_SUMMARY_SHARE_KEY, hashKey);
        recentBillJobSummaryRepository.save(hashKey, billAiSummaryEntity);
    }

    private BackgroundRequestStatusEntity getBackgroundRequestStatusEntity(JobExecution jobExecution) {
        super.setCurrentExecutionContext(jobExecution.getExecutionContext());
        String recentBillHashKey = super.getDataFromJobExecutionContext(RecentBillConstant.BILL_AI_BACKGROUND_REQUEST_SHARE_KEY);
        return backgroundRequestStatusRepository.findByKey(recentBillHashKey);
    }

}
