package com.barlow.infra.storage;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.AuthProvider;
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
	int changeRole(@Param("userNo") Long userNo, @Param("role") User.Role role);

	@Query("""
		    SELECT u
		    FROM ExternalAuthJpaEntity ap
		    JOIN UserJpaEntity u ON u.no = ap.memberNo
		    WHERE ap.provider = :authProvider AND ap.sub = :sub
		""")
	UserJpaEntity findByProviderAndSub(@Param("authProvider") AuthProvider authProvider, @Param("sub") String sub);
}
