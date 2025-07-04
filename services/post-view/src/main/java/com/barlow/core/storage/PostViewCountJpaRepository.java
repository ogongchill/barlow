package com.barlow.core.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PostViewCountJpaRepository extends JpaRepository<BillPostJpaEntity, String> {

	@Modifying
	@Query("UPDATE BillPostJpaEntity bp SET bp.viewCount = bp.viewCount + 1 WHERE bp.billId = :postId")
	void updateViewCount(@Param("postId") String postId);
}
