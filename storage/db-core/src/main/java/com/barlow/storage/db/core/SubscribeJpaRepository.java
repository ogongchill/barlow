package com.barlow.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeJpaRepository extends JpaRepository <SubscribeJpaEntity, Long> {

    List<SubscribeJpaEntity> findAllByMemberNo(Long memberNo);

    SubscribeJpaEntity findBySubscribeLegislationAccountNoAndMemberNo(Long subscribeLegislationAccountNo, Long memberNo);

    void deleteBySubscribeLegislationAccountNoAndMemberNo(Long subscribeLegislationAccountNo, Long memberNo);
}
