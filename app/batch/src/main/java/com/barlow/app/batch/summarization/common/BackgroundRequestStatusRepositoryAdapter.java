package com.barlow.app.batch.summarization.common;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BackgroundRequestStatusRepositoryAdapter implements BackgroundRequestStatusRepository{

    private static final Map<String, BackgroundRequestStatusEntity> STORAGE = new HashMap<>();

    @Override
    public void save(String key, BackgroundRequestStatusEntity backgroundRequestStatusEntity) {
        STORAGE.put(key, backgroundRequestStatusEntity);
    }

    @Override
    public BackgroundRequestStatusEntity findByKey(String key) {
        return STORAGE.get(key);
    }
}
