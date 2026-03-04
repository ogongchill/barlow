package com.barlow.infra.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TermAgreementJpaRepository extends JpaRepository<TermAgreementJpaEntity, Long> {

    void deleteAllByMemberNo(Long memberNo);
}
