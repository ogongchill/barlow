package com.barlow.app.batch.summarization.step.request;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
@StepScope
public class TodayReceivedBillReader extends AbstractExecutionContextSharingManager implements ItemReader<TodayBillInfoBatchEntity.BillInfoItem>, StepExecutionListener {

    private final RecentBillJobScopeShareRepository recentBillJobScopeShareRepository;
    private final Deque<TodayBillInfoBatchEntity.BillInfoItem> items = new ArrayDeque<>();

    public TodayReceivedBillReader(RecentBillJobScopeShareRepository recentBillJobScopeShareRepository) {
        this.recentBillJobScopeShareRepository = recentBillJobScopeShareRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
        super.setCurrentExecutionContext(jobExecutionContext);
        String key = super.getDataFromJobExecutionContext(RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY);
        TodayBillInfoBatchEntity todayBillInfoBatchEntity = recentBillJobScopeShareRepository.findByKey(key);
        items.addAll(todayBillInfoBatchEntity.items());
    }

    @Override
    public TodayBillInfoBatchEntity.BillInfoItem read() {
        return items.poll();
    }
}
