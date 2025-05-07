package com.barlow.core.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	UserJpaEntity findByNo(Long no);
}
