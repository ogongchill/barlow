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

class AllowUnofficialReleaseStrategyTest {

	@DisplayName("버전 정보에 따라 업데이트 여부를 반환하는지 확인")
	@ParameterizedTest
	@MethodSource("generateVersionData")
	void evaluate(
		SemanticVersion clientVersion,
		AvailableClientVersion availableClientVersion,
		ClientVersionStatus expected
	) {
		ClientVersionUpdateStrategy strategy = new AllowUnofficialReleaseStrategy();
		ClientVersionStatus actual = strategy.evaluate(clientVersion, availableClientVersion);
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> generateVersionData() {
		AvailableClientVersion availableClientVersion = new AvailableClientVersion(
			SemanticVersion.of("1.1.0"),
			SemanticVersion.of("2.0.0")
		);
		return Stream.of(
			Arguments.of(SemanticVersion.of("1.0.0-rc"), availableClientVersion, NEED_FORCE_UPDATE),
			Arguments.of(SemanticVersion.of("1.0.0"), availableClientVersion, NEED_FORCE_UPDATE),
			Arguments.of(SemanticVersion.of("2.0.0"), availableClientVersion, LATEST),
			Arguments.of(SemanticVersion.of("1.1.0"), availableClientVersion, UPDATE_AVAILABLE)
		);
	}
}