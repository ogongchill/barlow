package com.barlow.core.storage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LegislationAccountJpaRepository extends JpaRepository<LegislationAccountJpaEntity, Long> {

	@Query("""
		SELECT la FROM LegislationAccountJpaEntity la
		INNER JOIN SubscribeJpaEntity s ON la.no = s.subscribeLegislationAccountNo
		WHERE s.memberNo = :memberNo""")
	List<LegislationAccountJpaEntity> findSubscribedLegislationAccountsByMemberNo(@Param("memberNo") Long memberNo);

	LegislationAccountJpaEntity findByNo(Long accountNo);

	@Modifying
	@Query("""
		UPDATE LegislationAccountJpaEntity la
		SET la.subscriberCount = la.subscriberCount + 1
		WHERE la.no = :accountNo""")
	void updateIncrementSubscriberCount(@Param("accountNo") Long accountNo);

	@Modifying
	@Query("""
		UPDATE LegislationAccountJpaEntity la
		SET la.subscriberCount = la.subscriberCount - 1
		WHERE la.no = :accountNo""")
	void updateDecrementSubscriberCount(@Param("accountNo") Long accountNo);
}
