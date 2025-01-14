package com.barlow.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribeJpaRepository extends JpaRepository <SubscribeJpaEntity, Long> {

    @Query("SELECT d FROM SubscribeJpaEntity d WHERE d.memberNo = :memberNo")
    List<SubscribeJpaEntity> retrieveByMemberNo(@Param("memberNo") Long memberNo);
}
