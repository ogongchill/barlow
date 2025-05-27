package com.barlow.core.domain.version;

import com.barlow.core.enumerate.VersionSuffix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SemanticVersionTest {

    @DisplayName("semanticversion의 정규식을 통해 생성되는지 확인")
    @ParameterizedTest
    @MethodSource("generateSemanticData")
    void testOf(String value, int major, int minor, int patch, VersionSuffix suffix) {
        SemanticVersion actual = SemanticVersion.of(value);
        SemanticVersion expected = new SemanticVersion(major, minor, patch, suffix);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> generateSemanticData() {
        return Stream.of(
            Arguments.of("1.0.0", 1, 0, 0, VersionSuffix.NONE),
            Arguments.of("1.0.0-alpha", 1, 0, 0, VersionSuffix.ALPHA),
            Arguments.of("1.0.0-beta", 1, 0, 0, VersionSuffix.BETA),
            Arguments.of(("1.2.2-snapshot"), 1, 2, 2, VersionSuffix.SNAPSHOT),
            Arguments.of(("3.10.3-rc"), 3, 10, 3, VersionSuffix.RC)
        );
    }

    @DisplayName("sematicVersion의 suffix 간 하위 버전을 제대로 인식하는지 확인")
    @ParameterizedTest
    @MethodSource("generateSuffixCompareData")
    void testSuffixIsLessThan(String left, String right, boolean expected) {
        boolean actual = SemanticVersion.of(left).isLessThan(SemanticVersion.of(right));
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> generateSuffixCompareData() {
        return Stream.of(
                Arguments.of("1.0.0-snapshot", "1.0.0-alpha", true),
                Arguments.of("1.0.0-snapshot", "1.0.0-snapshot", false),
                Arguments.of("1.0.0-alpha", "1.0.0-beta", true),
                Arguments.of("1.0.0-beta", "1.0.0-rc", true),
                Arguments.of("1.0.0-rc", "1.0.0", true)
        );
    }

    @DisplayName("sematicVersion의 suffix 간 하위 버전을 제대로 인식하는지 확인")
    @ParameterizedTest
    @MethodSource("generateCompareData")
    void isLessThan(String left, String right, boolean expected) {
        boolean actual = SemanticVersion.of(left).isLessThan(SemanticVersion.of(right));
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> generateCompareData() {
        return Stream.of(
                Arguments.of("1.0.0", "1.0.1", true),
                Arguments.of("1.0.0", "1.0.0", false),
                Arguments.of("1.0.1", "1.1.0", true),
                Arguments.of("1.1.1", "2.1.0", true)
        );
    }
}