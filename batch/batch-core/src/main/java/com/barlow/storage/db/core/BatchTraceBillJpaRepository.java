package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BatchTraceBillJpaRepository extends CrudRepository<BatchTraceBillJpaEntity, Long> {

	List<BatchTraceBillJpaEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
