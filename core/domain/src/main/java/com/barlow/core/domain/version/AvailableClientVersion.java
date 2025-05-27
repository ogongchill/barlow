package com.barlow.core.domain.version;

public record AvailableClientVersion (
    SemanticVersion minimumClientVersion,
    SemanticVersion latestClientVersion){
}
