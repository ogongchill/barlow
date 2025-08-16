package com.barlow.app.batch.summarization.common;


public interface RecentBillJobSummaryRepository {

    void save(String key, BillAiSummaryEntity billSummaryEntity);

    BillAiSummaryEntity findByKey(String key);
}
