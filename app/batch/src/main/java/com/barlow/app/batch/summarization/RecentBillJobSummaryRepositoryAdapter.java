package com.barlow.app.batch.summarization;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RecentBillJobSummaryRepositoryAdapter implements RecentBillJobSummaryRepository{

    private static Map<String, BillAiSummaryEntity> STORAGE = new HashMap<>();

    @Override
    public void save(String key, BillAiSummaryEntity value) {
        STORAGE.put(key, value);
    }

    @Override
    public BillAiSummaryEntity findByKey(String key) {
        return STORAGE.get(key);
    }
}
