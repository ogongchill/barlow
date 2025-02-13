package com.barlow.core.domain.notification;

import org.jetbrains.annotations.NotNull;

public record NotifiableTopic(
        @NotNull String name,
        @NotNull String korName,
        @NotNull String iconUrl
) {
}
