package com.barlow.core.domain.version;

import static com.barlow.core.enumerate.ClientVersionStatus.LATEST;
import static com.barlow.core.enumerate.ClientVersionStatus.NEED_FORCE_UPDATE;
import static com.barlow.core.enumerate.ClientVersionStatus.UPDATE_AVAILABLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.barlow.core.enumerate.ClientVersionStatus;
import com.barlow.core.enumerate.DeviceOs;

class ClientVersionPolicyTest {

	@DisplayName("버전 정책에 따라 클라이언트 버전 상태를 반환한다.")
	@ParameterizedTest
	@MethodSource("generateVersionData")
	void evaluate_VersionPolicy_ReturnsExpectedStatus(SemanticVersion clientVersion, ClientVersionStatus expected) {
		// given
		ClientVersionPolicy policy = ClientVersionPolicy.of(
			DeviceOs.ANDROID, SemanticVersion.of("1.1.0"), SemanticVersion.of("2.0.0"));

		// when
		ClientVersionStatus actual = policy.evaluate(clientVersion);

		// then
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> generateVersionData() {
		return Stream.of(
			Arguments.of(SemanticVersion.of("1.0.0"), NEED_FORCE_UPDATE),
			Arguments.of(SemanticVersion.of("1.0.9"), NEED_FORCE_UPDATE),
			Arguments.of(SemanticVersion.of("1.1.0"), UPDATE_AVAILABLE),
			Arguments.of(SemanticVersion.of("1.9.9"), UPDATE_AVAILABLE),
			Arguments.of(SemanticVersion.of("2.0.0"), LATEST));
	}
}
