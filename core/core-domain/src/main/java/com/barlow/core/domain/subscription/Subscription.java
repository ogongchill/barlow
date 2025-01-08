package com.barlow.core.domain.subscription;

import org.jetbrains.annotations.NotNull;

public record Subscription(
        @NotNull Long no,
        @NotNull Long memberNo,
        @NotNull Long legislationAccountNo
) {
}
