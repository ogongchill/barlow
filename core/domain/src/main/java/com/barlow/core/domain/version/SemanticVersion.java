package com.barlow.core.domain.version;

import com.barlow.core.enumerate.VersionSuffix;

import java.util.Objects;
import java.util.regex.Pattern;

public final class SemanticVersion {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+(-[a-zA-Z0-9]+)?$");
    private final int major;
    private final int minor;
    private final int patch;
    private final VersionSuffix versionSuffix;

    public SemanticVersion(int major, int minor, int patch, VersionSuffix versionSuffix) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.versionSuffix = versionSuffix;
    }

    public static SemanticVersion of(String versionValue) {
        if(!VERSION_PATTERN.matcher(versionValue).matches()) {
            throw new IllegalArgumentException();
        }
        String[] parts = versionValue.split("[.-]");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        if(parts.length == 4) {
            return new SemanticVersion(major, minor, patch, VersionSuffix.fromString(parts[3]));
        }
        return new SemanticVersion(major, minor, patch, VersionSuffix.NONE);
    }

    public boolean isLessThan(SemanticVersion other) {
        return this.compareTo(other) < 0;
    }

    private int compareTo(SemanticVersion other) {
        if (major != other.major) return Integer.compare(major, other.major);
        if (minor != other.minor) return Integer.compare(minor, other.minor);
        if (patch != other.patch) return Integer.compare(patch, other.patch);
        return Integer.compare(versionSuffix.getPriority(), other.versionSuffix.getPriority());
    }

    public boolean isOfficialRelease() {
        return versionSuffix == VersionSuffix.NONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticVersion that = (SemanticVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch && versionSuffix == that.versionSuffix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, versionSuffix);
    }
}
