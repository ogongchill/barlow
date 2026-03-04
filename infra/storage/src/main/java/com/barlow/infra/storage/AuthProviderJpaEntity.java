package com.barlow.infra.storage;

import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.authprovider.UserAuthProviderCreateCommand;
import com.barlow.core.enumerate.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "auth_provider")
public class AuthProviderJpaEntity extends BaseTimeJpaEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_provider_no")
    private Long authProviderNo;

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider;

    @Column(name = "sub", nullable = false)
    private String sub;

    public AuthProviderJpaEntity(Long memberNo, AuthProvider provider, String sub) {
        this.memberNo = memberNo;
        this.provider = provider;
        this.sub = sub;
    }

    public AuthProviderJpaEntity() {

    }

    public static AuthProviderJpaEntity fromCommand(UserAuthProviderCreateCommand command) {
        return new AuthProviderJpaEntity(command.userNo(), command.authProvider(), command.sub());
    }

    public ExternalPrincipal toExternalPrincipal() {
        return new ExternalPrincipal(provider, sub);
    }
}
