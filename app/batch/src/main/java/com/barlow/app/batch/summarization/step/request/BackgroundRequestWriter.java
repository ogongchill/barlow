package com.barlow.app.batch.summarization.step.request;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.RecentBillConstant;
import com.barlow.app.batch.summarization.common.BackgroundRequestStatusEntity;
import com.barlow.app.batch.summarization.common.BackgroundRequestStatusRepository;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.app.batch.utils.HashUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@StepScope
public class BackgroundRequestWriter extends AbstractExecutionContextSharingManager implements ItemWriter<SummaryRequestStatus>, StepExecutionListener {

    private final BackgroundRequestStatusRepository backgroundRequestStatusRepository;
    private final Map<String, SummaryRequestStatus> aggregationMap = new LinkedHashMap<>();

    public BackgroundRequestWriter(BackgroundRequestStatusRepository backgroundRequestStatusRepository) {
        this.backgroundRequestStatusRepository = backgroundRequestStatusRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.setCurrentExecutionContext(stepExecution.getJobExecution().getExecutionContext());
    }

    @Override
    public void write(Chunk<? extends SummaryRequestStatus> chunk) {
        chunk.getItems()
                .stream()
                .filter(Objects::nonNull)
                .forEach(item -> aggregationMap.put(item.billId(), item));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        BackgroundRequestStatusEntity entity = new BackgroundRequestStatusEntity(
                LocalDate.now(),
                List.copyOf(aggregationMap.values())
        );
        String newHashKey = HashUtil.generate(entity);
        super.putDataToExecutionContext(RecentBillConstant.BILL_AI_BACKGROUND_REQUEST_SHARE_KEY, newHashKey);
        backgroundRequestStatusRepository.save(newHashKey, entity);
        return ExitStatus.COMPLETED;
    }
}
