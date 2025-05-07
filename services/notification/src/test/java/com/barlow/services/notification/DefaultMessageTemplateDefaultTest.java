package com.barlow.services.notification;

import static com.barlow.services.notification.DefaultMessageTemplate.Default;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultMessageTemplateDefaultTest {

	@DisplayName("기본법안알림 주제를 받아서 해당하는 Default 타입을 찾아온다")
	@ParameterizedTest
	@MethodSource("provideDefaultNotificationTopicAndExpectDefaultType")
	void Default_findByValue(String topicName, Default expect) {
		Default actual = Default.findByValue(topicName);

		assertThat(actual).isEqualTo(expect);
	}

	@DisplayName("기본법안알림 주제를 받아서 해당하는 Default 타입이 없으면 예외를 발생시킨다")
	@Test
	void Default_findByValue_failure() {
		assertThatThrownBy(() -> Default.findByValue("notExist"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 Default 법안 알림 주제입니다");
	}

	private static Stream<Arguments> provideDefaultNotificationTopicAndExpectDefaultType() {
		return Stream.of(
			Arguments.of("접수", Default.RECEIPT),
			Arguments.of("본회의부의안건", Default.SUBMISSION_PLENARY_SESSION),
			Arguments.of("본회의의결", Default.RESOLUTION_PLENARY_SESSION),
			Arguments.of("재의요구", Default.RECONSIDERATION_GOVERNMENT),
			Arguments.of("공포", Default.PROMULGATION)
		);
	}
}