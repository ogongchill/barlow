package com.barlow.core.storage;

import com.barlow.core.enumerate.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderJpaRepository extends JpaRepository<AuthProviderJpaEntity, Long> {

    boolean existsByProviderAndSub(AuthProvider authProvider, String sub);

    boolean findByProviderAndSub(AuthProvider authProvider, String sub);

    boolean existsByUserId(Long userId);
}
