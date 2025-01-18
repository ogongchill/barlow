package com.barlow.core.domain.notification;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NotificationConfigRetrieveService.class)
class NotificationConfigRetrieveServiceTest {

    @Autowired
    private NotificationConfigRetrieveService notificationConfigRetrieveService;

    @MockitoBean
    private NotificationConfigRepository notificationConfigRepository;

    @DisplayName("알림 조회시 항목이 없으면 예외 반환 확인")
    @Test
    void retrieveMemberNotificationConfig() {
        List<NotificationConfig> empty = new ArrayList<>();
        Long memberNo = 1L;
        when(notificationConfigRepository.retrieveByMemberNo(memberNo))
                .thenReturn(empty);
        assertThatThrownBy(() -> notificationConfigRetrieveService.retrieveMemberNotificationConfig(memberNo))
                .isInstanceOf(MemberNotificationConfigException.class)
                .hasMessage("사용자 1에 대한 알림정보가 없습니다")
                .satisfies(rawException -> {
                            CoreDomainException coreDomainException = (CoreDomainException) rawException;
                            assertThat(coreDomainException.getCode()).isEqualTo(CoreDomainExceptionCode.E404);
                            assertThat(coreDomainException.getLevel()).isEqualTo(CoreDomainExceptionLevel.IMPLEMENTATION);
                        }
                );
    }
}