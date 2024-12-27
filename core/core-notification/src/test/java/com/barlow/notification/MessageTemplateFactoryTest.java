package com.barlow.notification;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageTemplateFactoryTest {

	@DisplayName("알림타입 값을 받아서 해당 타입에 맞는 메시지 템플릿을 가져온다")
	@ParameterizedTest
	@MethodSource("provideNotificationTypeNameAndExpectMessageTemplate")
	void getBy(String typeName, Class<MessageTemplate> expect) {
		NotificationType notificationType = NotificationType.valueOf(typeName);

		MessageTemplate actual = MessageTemplateFactory.getBy(notificationType);

		assertThat(actual).isInstanceOf(expect);
	}

	private static Stream<Arguments> provideNotificationTypeNameAndExpectMessageTemplate() {
		return Stream.of(
			Arguments.of("DEFAULT", DefaultMessageTemplate.class),
			Arguments.of("STANDING_COMMITTEE", CommitteeMessageTemplate.class)
		);
	}
}