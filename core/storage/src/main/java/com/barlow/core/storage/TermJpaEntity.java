package com.barlow.core.storage;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class TermJpaEntity extends BaseTimeJpaEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "effective_at", nullable = false)
    private LocalDateTime effectiveAt;
}
