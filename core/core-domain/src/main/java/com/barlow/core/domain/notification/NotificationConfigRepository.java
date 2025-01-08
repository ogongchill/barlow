package com.barlow.core.domain.notification;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationConfigRepository {

    List<NotificationConfig> retrieveByMemberNo(Long memberNo);
}
