package com.barlow.app.batch.summarization.common;

public interface BackgroundRequestStatusRepository {

    void save(String key, BackgroundRequestStatusEntity backgroundRequestStatusEntity);

    BackgroundRequestStatusEntity findByKey(String key);
}
