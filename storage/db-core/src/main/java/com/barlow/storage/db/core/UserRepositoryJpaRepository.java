package com.barlow.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	UserJpaEntity findByNo(Long no);
}
