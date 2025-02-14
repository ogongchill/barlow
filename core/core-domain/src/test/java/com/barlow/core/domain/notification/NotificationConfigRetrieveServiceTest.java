package com.barlow.core.domain.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NotificationConfigRetrieveService.class)
class NotificationConfigRetrieveServiceTest {

	@Autowired
	private NotificationConfigRetrieveService notificationConfigRetrieveService;

	@MockitoBean
	private NotificationConfigRepository notificationConfigRepository;

	@DisplayName("알림 조회시 항목이 없으면 예외처리 하지 않고 빈 항목을 반환하는지 확인")
	@Test
	void retrieveMemberNotificationConfig() {
		List<NotificationConfig> empty = new ArrayList<>();
		Long memberNo = 1L;
		when(notificationConfigRepository.retrieveByMemberNo(memberNo))
			.thenReturn(empty);
		assertDoesNotThrow(() -> notificationConfigRetrieveService.retrieveMemberNotificationConfig(memberNo));
	}
}