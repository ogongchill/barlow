package com.barlow.infra.storage;

import com.barlow.core.enumerate.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExternalAuthJpaRepository extends JpaRepository<ExternalAuthJpaEntity, Long> {

	boolean existsByProviderAndSub(AuthProvider authProvider, String sub);

	List<ExternalAuthJpaEntity> findAllByMemberNo(Long memberNo);

	void deleteAllByMemberNo(Long memberNo);
}
