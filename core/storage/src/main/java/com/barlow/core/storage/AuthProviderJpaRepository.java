package com.barlow.core.storage;

import com.barlow.core.enumerate.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthProviderJpaRepository extends JpaRepository<AuthProviderJpaEntity, Long> {

    boolean existsByProviderAndSub(AuthProvider authProvider, String sub);

    boolean existsByMemberNoAndProvider(Long memberNo, AuthProvider provider);

    List<AuthProviderJpaEntity> findAllByMemberNo(Long memberNo);
}

