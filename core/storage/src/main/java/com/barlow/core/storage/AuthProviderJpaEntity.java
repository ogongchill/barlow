package com.barlow.core.storage;

import com.barlow.core.enumerate.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AuthProviderJpaEntity extends BaseTimeJpaEntity{

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider;

    @Column(name = "sub", nullable = false)
    private String sub;
}
