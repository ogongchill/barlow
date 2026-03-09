package com.barlow.core.domain.version;

import com.barlow.core.enumerate.VersionSuffix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SemanticVersionTest {

	@DisplayName("유효한 버전 문자열로 생성하면 각 구성 요소가 올바르게 파싱된다.")
	@ParameterizedTest
	@MethodSource("generateSemanticData")
	void of_ValidVersionString_ParsesCorrectly(String value, int major, int minor, int patch, VersionSuffix suffix) {
		// when
		SemanticVersion actual = SemanticVersion.of(value);

		// then
		assertThat(actual).isEqualTo(new SemanticVersion(major, minor, patch, suffix));
	}

	private static Stream<Arguments> generateSemanticData() {
		return Stream.of(
			Arguments.of("1.0.0", 1, 0, 0, VersionSuffix.NONE),
			Arguments.of("1.0.0-alpha", 1, 0, 0, VersionSuffix.ALPHA),
			Arguments.of("1.0.0-beta", 1, 0, 0, VersionSuffix.BETA),
			Arguments.of("1.2.2-snapshot", 1, 2, 2, VersionSuffix.SNAPSHOT),
			Arguments.of("3.10.3-rc", 3, 10, 3, VersionSuffix.RC));
	}

	@DisplayName("지원하지 않는 버전 형식으로 생성하면 예외가 발생한다.")
	@Test
	void of_InvalidVersionFormat_ThrowsClientVersionException() {
		// given
		String invalidVersion = "not-a-version";

		// when & then
		assertThatThrownBy(() -> SemanticVersion.of(invalidVersion))
			.isInstanceOf(ClientVersionException.class);
	}

	@DisplayName("suffix 순서에 따라 동일 버전 번호의 하위 여부를 판단한다.")
	@ParameterizedTest
	@MethodSource("generateSuffixCompareData")
	void isLessThan_DifferentSuffix_ReturnsExpected(String left, String right, boolean expected) {
		// when
		boolean actual = SemanticVersion.of(left).isLessThan(SemanticVersion.of(right));

		// then
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> generateSuffixCompareData() {
		return Stream.of(
			Arguments.of("1.0.0-snapshot", "1.0.0-alpha", true),
			Arguments.of("1.0.0-snapshot", "1.0.0-snapshot", false),
			Arguments.of("1.0.0-alpha", "1.0.0-beta", true),
			Arguments.of("1.0.0-beta", "1.0.0-rc", true),
			Arguments.of("1.0.0-rc", "1.0.0", true));
	}

	@DisplayName("버전 숫자 비교로 하위 버전 여부를 판단한다.")
	@ParameterizedTest
	@MethodSource("generateCompareData")
	void isLessThan_DifferentVersionNumber_ReturnsExpected(String left, String right, boolean expected) {
		// when
		boolean actual = SemanticVersion.of(left).isLessThan(SemanticVersion.of(right));

		// then
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> generateCompareData() {
		return Stream.of(
			Arguments.of("1.0.0", "1.0.1", true),
			Arguments.of("1.0.0", "1.0.0", false),
			Arguments.of("1.0.1", "1.1.0", true),
			Arguments.of("1.1.1", "2.1.0", true));
	}
}
