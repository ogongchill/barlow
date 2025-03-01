package com.barlow.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceJpaRepository extends JpaRepository<DeviceJpaEntity, Long> {

	DeviceJpaEntity findByDeviceId(String deviceId);

	@Modifying
	@Query("UPDATE DeviceJpaEntity d SET d.token = :deviceToken WHERE d.deviceId = :deviceId")
	void updateDeviceToken(@Param("deviceId") String deviceId, @Param("deviceToken") String deviceToken);
}
