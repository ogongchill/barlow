package com.barlow.core.domain.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberNotificationConfigTest {

    @DisplayName("빈 항목으로 생성 시 예외처리 하지 않음 확인")
    @Test
    void testNotThrowExceptionWhenEmpty() {
        assertDoesNotThrow(() -> new MemberNotificationConfig(new ArrayList<>()));
    }
}