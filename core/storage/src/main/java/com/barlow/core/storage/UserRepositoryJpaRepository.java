package com.barlow.core.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryJpaRepository extends JpaRepository<UserJpaEntity, Long> {

	UserJpaEntity findByNo(Long no);

	@Modifying
	@Query("DELETE FROM UserJpaEntity u WHERE u.no = :userNo")
	void deleteByUserNo(@Param("userNo") Long userNo);
}
