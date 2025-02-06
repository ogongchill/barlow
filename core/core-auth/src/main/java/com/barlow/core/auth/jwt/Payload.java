package com.barlow.core.auth.jwt;

import com.barlow.core.auth.exception.CoreAuthException;
import com.barlow.core.auth.exception.CoreAuthExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payload {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, String> claims;

    public Payload(Map<String, String> claims) {
        this.claims = claims;
    }

    public static Payload empty() {
        return new Payload(new HashMap<>());
    }

    public static Payload fromJson(String jsonString) {
        try {
            Map<String, String> claims = MAPPER.readValue(jsonString, new TypeReference<Map<String, String>>() {});
            return new Payload(claims);
        } catch (JsonProcessingException e) {
            throw new CoreAuthException(CoreAuthExceptionType.INVALID_PAYLOAD, "옳바르지 않은 json형식 입니다");
        }
    }

    public static PayloadCreator withIssuer(String issuer) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ReservedClaims.ISSUER.getValue(), issuer);
        return new PayloadCreator(claims);
    }

    public String toJsonString() {
       try {
           return MAPPER.writeValueAsString(claims);
       } catch (JsonProcessingException e) {
           throw new CoreAuthException(CoreAuthExceptionType.INVALID_PAYLOAD, "json으로 변환 할 수 없습니다");
       }
    }

    public String getIssuer() {
        return claims.get(ReservedClaims.ISSUER.getValue());
    }

    public String getClaim(String claim) {
        return claims.get(claim);
    }

    public static class PayloadCreator {

        private final Map<String, String> claims;

        public PayloadCreator(Map<String, String> claims) {
            this.claims = claims;
        }

        public PayloadCreator withClaim(String claim, String value) {
            claims.put(claim, value);
            return this;
        }

        public PayloadCreator withExpiration(Date expirationDate) {
            Long expiration = expirationDate.toInstant().toEpochMilli();
            return withExpiration(expiration);
        }

        public PayloadCreator withExpiration(Long expiration) {
            claims.put(ReservedClaims.EXPIRATION.getValue(), String.valueOf(expiration));
            return this;
        }

        public Payload create() {
            return new Payload(Map.copyOf(claims));
        }
    }
}
