package com.barlow.infra.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

	List<SubscriptionJpaEntity> findAllByMemberNo(Long memberNo);

	SubscriptionJpaEntity findBySubscribeLegislationAccountNoAndMemberNo(Long subscribeLegislationAccountNo,
		Long memberNo);

	@Modifying
	@Query("""
		DELETE FROM SubscriptionJpaEntity s
		WHERE s.subscribeLegislationAccountNo = :accountNo
		AND s.memberNo = :memberNo""")
	void deleteBySubscribeLegislationAccountNoAndMemberNo(@Param("accountNo") Long subscribeLegislationAccountNo,
		@Param("memberNo") Long memberNo);

	@Modifying
	@Query("DELETE FROM SubscriptionJpaEntity s WHERE s.memberNo = :memberNo")
	void deleteAllByMemberNo(@Param("memberNo") Long memberNo);
}
