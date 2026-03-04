package com.barlow.infra.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermJpaRepository extends JpaRepository<TermJpaEntity, Long> {

    @Query("SELECT t FROM TermJpaEntity t WHERE t.no IN (SELECT at.termNo FROM ActiveTermJpaEntity at)")
    List<TermJpaEntity> findAllByActiveTerms();
}
