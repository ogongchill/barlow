package com.barlow.storage.db.core;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceJpaRepository extends JpaRepository<DeviceJpaEntity, Long> {

	@Query("SELECT d FROM DeviceJpaEntity d WHERE d.status = 'ACTIVE' AND d.memberNo IN :memberNos")
	List<DeviceJpaEntity> findAllByStatusActiveAndMemberNoIn(Set<Long> memberNos);
}
