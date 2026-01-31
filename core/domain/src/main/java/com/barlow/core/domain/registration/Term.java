package com.barlow.core.domain.registration;

public record Term(
        Long id,
        String title,
        String version,
        String linkUrl,
        boolean required
) {

    public enum Type {

        SERVICE,
        PRIVACY,
        MARKETING,
        ;
    }
}
