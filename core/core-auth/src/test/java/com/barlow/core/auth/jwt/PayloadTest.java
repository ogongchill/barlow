package com.barlow.core.auth.jwt;

import com.barlow.core.auth.exception.CoreAuthException;
import com.barlow.core.auth.exception.CoreAuthExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PayloadTest {

    @DisplayName("json을 통해 생성되는지 확인")
    @Test
    void testFromJson() {
        String jsonString = """
                {"iss":"issuer","claim":"1"}""";
        Payload payload = Payload.fromJson(jsonString);
        assertAll(
                () -> assertThat(payload.getIssuer()).isEqualTo("issuer"),
                () -> assertThat(payload.getClaim("claim")).isEqualTo("1")
        );
    }

    @DisplayName("json으로 변환되는지 확인")
    @Test
    void testToJsonString() {
        Payload payload2 = Payload.empty();
        Payload payload = Payload.withIssuer("issuer")
                .withClaim("claim", "1")
                .create();
        String jsonString = payload.toJsonString();
        assertThat(jsonString)
                .contains("\"claim\":\"1\"")
                .contains("\"iss\":\"issuer\"");

    }

    @DisplayName("json형식이 아니면 예외처리")
    @Test
    void testThrowExceptionWhenInvalidJson() {
        String invalidJson = """
                {"issuer":}""";
        assertThatThrownBy(()->Payload.fromJson(invalidJson))
                .isInstanceOf(CoreAuthException.class)
                .satisfies(e -> {
                    CoreAuthException exception = (CoreAuthException) e;
                    assertThat(exception.getType()).isEqualTo(CoreAuthExceptionType.INVALID_PAYLOAD);
                } );
    }
}