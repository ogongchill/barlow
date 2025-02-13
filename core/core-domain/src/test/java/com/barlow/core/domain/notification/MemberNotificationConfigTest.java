package com.barlow.core.domain.notification;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberNotificationConfigTest {

    @DisplayName("빈 항목으로 생성 시 예외처리 하지 않음 확인")
    @Test
    void testNotThrowExceptionWhenEmpty() {
        assertDoesNotThrow(() -> new MemberNotificationConfig(new ArrayList<>()));
    }

    @DisplayName("memberNo가 일치하지 않은 항목으로 생성시 MemberNotificationConfigException반환 확인")
    @Test
    void testThrowExceptionWhenMisMatch() {
        Long memberNo = 1L;
        NotifiableTopic sampleTopic = new NotifiableTopic("topic1", "1", "");
        NotifiableTopic sampleTopicAnother = new NotifiableTopic("topic2", "2", "");
        List<NotificationConfig> notificationConfigs = List.of(
                new NotificationConfig(1L, memberNo, true, sampleTopic),
                new NotificationConfig(2L, 2L, false, sampleTopicAnother));
        assertThatThrownBy(() -> new MemberNotificationConfig(notificationConfigs))
                .isInstanceOf(MemberNotificationConfigException.class)
                .satisfies(rawException -> {
                    CoreDomainException coreDomainException = (CoreDomainException) rawException;
                    assertThat(coreDomainException.getCode()).isEqualTo(CoreDomainExceptionType.ILLEGAL_STATE_EXCEPTION.getCode());
                    assertThat(coreDomainException.getLevel()).isEqualTo(CoreDomainExceptionType.ILLEGAL_STATE_EXCEPTION.getLevel());
                    System.out.println(coreDomainException.getMessage());
                });
    }
}