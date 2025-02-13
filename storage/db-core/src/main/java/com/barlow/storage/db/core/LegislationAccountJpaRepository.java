package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LegislationAccountJpaRepository extends JpaRepository<LegislationAccountJpaEntity, Long> {

	@Query("""
		SELECT la FROM LegislationAccountJpaEntity la
		INNER JOIN SubscribeJpaEntity s ON la.no = s.subscribeLegislationAccountNo
		WHERE s.memberNo = :memberNo""")
	List<LegislationAccountJpaEntity> findSubscribedLegislationAccountsByMemberNo(@Param("memberNo") Long memberNo);
}
