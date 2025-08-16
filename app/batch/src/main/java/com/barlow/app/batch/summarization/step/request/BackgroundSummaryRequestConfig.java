package com.barlow.app.batch.summarization.step.request;

import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity.BillInfoItem;
import com.barlow.app.batch.summarization.common.ChatRequestFactory;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.client.ai.openai.api.OpenAiApiPort;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import com.barlow.core.enumerate.ProgressStatus;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BackgroundSummaryRequestConfig {

    @Bean
    public ItemProcessor<BillInfoItem, SummaryRequestStatus> backgroundRequestProcessor(
            ChatRequestFactory chatRequestFactory,
            OpenAiApiPort api
    ) {
        return item -> {
            if (item.summary() == null || item.progressStatus() != ProgressStatus.RECEIVED) {
                return null; // 본문이 없는 경우나 접수가 아닌 경우 요약 안함
            }
            ChatRequest req = chatRequestFactory.backgroundSummaryRequestFrom(item.summary());
            OpenAiResponse resp = api.getResponse(req);
            return new SummaryRequestStatus(item.billId(), resp.id(), resp.status());
        };
    }

    @Bean
    public AsyncItemProcessor<BillInfoItem, SummaryRequestStatus> asyncBackgroundSummaryProcessor(
            ItemProcessor<BillInfoItem, SummaryRequestStatus> backgroundRequestProcessor,
            @Qualifier("aiSummaryAsyncExecutor") TaskExecutor aiSummaryAsyncExecutor
            ) {
        AsyncItemProcessor<BillInfoItem, SummaryRequestStatus> processor = new AsyncItemProcessor<>();
        processor.setDelegate(backgroundRequestProcessor);
        processor.setTaskExecutor(aiSummaryAsyncExecutor);
        return processor;
    }

    @Bean
    @StepScope
    public AsyncItemWriter<SummaryRequestStatus> asyncBackgroundRequestWriter(
            ItemWriter<SummaryRequestStatus> backgroundRequestWriter
    ) {
        AsyncItemWriter<SummaryRequestStatus> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(backgroundRequestWriter);
        return asyncItemWriter;
    }

    @Bean
    public TaskExecutor aiSummaryAsyncExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(4);
        ex.setMaxPoolSize(8);
        ex.setQueueCapacity(100);
        ex.setThreadNamePrefix("ai-summary-");
        ex.initialize();
        return ex;
    }
}
