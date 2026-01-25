package com.barlow.core.storage;

import com.barlow.core.enumerate.AuthProvider;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

public class AuthProviderJpaEntity extends BaseTimeJpaEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "user_no", nullable = false)
    private Long userNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider;

    @Column(name = "sub", nullable = false)
    private String sub;
}
