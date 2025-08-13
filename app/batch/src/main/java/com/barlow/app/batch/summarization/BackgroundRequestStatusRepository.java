package com.barlow.app.batch.summarization;

public interface BackgroundRequestStatusRepository {

    void save(String key, BackgroundRequestStatusEntity backgroundRequestStatusEntity);

    BackgroundRequestStatusEntity findByKey(String key);
}
