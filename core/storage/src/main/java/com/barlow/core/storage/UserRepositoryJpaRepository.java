package com.barlow.core.storage;

import com.barlow.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryJpaRepository extends JpaRepository<UserJpaEntity, Long> {

	UserJpaEntity findByNo(Long no);

	@Modifying
	@Query("DELETE FROM UserJpaEntity u WHERE u.no = :userNo")
	void deleteByUserNo(@Param("userNo") Long userNo);

	@Modifying
	@Query("UPDATE UserJpaEntity u SET u.role = :role where u.no = :userNo")
	int changeRole(@Param("userNo") Long userNo, @Param("role")User.Role role);
}
