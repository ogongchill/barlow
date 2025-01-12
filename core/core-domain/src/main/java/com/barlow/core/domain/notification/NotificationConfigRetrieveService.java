package com.barlow.core.domain.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationConfigRetrieveService {

    private final NotificationConfigRepository notificationConfigRepository;

    public NotificationConfigRetrieveService(NotificationConfigRepository notificationConfigRepository) {
        this.notificationConfigRepository = notificationConfigRepository;
    }

    public MemberNotificationConfig retrieveMemberNotificationConfig(Long memberNo) {
        return new MemberNotificationConfig(notificationConfigRepository.retrieveByMemberNo(memberNo));
    }
}
