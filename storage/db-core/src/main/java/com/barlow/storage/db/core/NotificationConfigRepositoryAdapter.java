package com.barlow.storage.db.core;

import com.barlow.core.domain.notification.NotificationConfig;
import com.barlow.core.domain.notification.NotificationConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationConfigRepositoryAdapter implements NotificationConfigRepository {

    private final NotificationConfigJpaRepository notificationConfigJpaRepository;

    public NotificationConfigRepositoryAdapter(NotificationConfigJpaRepository notificationConfigJpaRepository) {
        this.notificationConfigJpaRepository = notificationConfigJpaRepository;
    }

    @Override
    public List<NotificationConfig> retrieveByMemberNo(Long memberNo) {
        return notificationConfigJpaRepository.findAllByMemberNo(memberNo)
                .stream()
                .map(NotificationConfigJpaEntity::toNotificationConfig)
                .toList();
    }
}
