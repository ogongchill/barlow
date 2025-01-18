package com.barlow.core.domain.notification;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionType;

public class MemberNotificationConfigException extends CoreDomainException {

    MemberNotificationConfigException(CoreDomainExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

    static MemberNotificationConfigException memberMismatchException(String message) {
        return new MemberNotificationConfigException(
                CoreDomainExceptionType.ILLEGAL_STATE_EXCEPTION,
                message
        );
    }

    static MemberNotificationConfigException notFoundTopicName(Long memberNo, String topicName) {
        return new MemberNotificationConfigException(
                CoreDomainExceptionType.NOT_FOUND_EXCEPTION,
                String.format("사용자 %d에 대한 %s topic 정보가 없습니다", memberNo, topicName)
        );
    }

    static MemberNotificationConfigException emptyNotification(Long memberNo) {
        return new MemberNotificationConfigException(
                CoreDomainExceptionType.NOT_FOUND_EXCEPTION,
                String.format("사용자 %d에 대한 알림정보가 없습니다", memberNo)
        );
    }
}
