package com.barlow.core.enumerate;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum VersionSuffix {
    SNAPSHOT(0, "snapshot"),
    ALPHA(1, "alpha"),
    BETA(2, "beta"),
    RC(3, "rc"),
    NONE(4, ""),
    ;

    private static final Map<String, VersionSuffix> MAP_BY_VERSION_NAME = Arrays.stream(values())
            .collect(Collectors.toMap(v -> v.name, v -> v));

    private final int priority;
    private final String name;

    VersionSuffix(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    public static VersionSuffix fromString(String versionName) {
        if (versionName == null || versionName.isBlank()) return NONE;
        return Optional.ofNullable(MAP_BY_VERSION_NAME.get(versionName.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException("Unknown suffix: " + versionName));
    }

    public int getPriority() {
        return this.priority;
    }

    public String getName() {
        return this.name;
    }
}
