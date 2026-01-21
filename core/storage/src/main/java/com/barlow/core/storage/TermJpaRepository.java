package com.barlow.core.storage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermJpaRepository extends JpaRepository<TermJpaEntity, Long> {

    List<TermJpaEntity> getAllByVersion(String version);
}
