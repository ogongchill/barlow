package com.barlow.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.DeviceOs;

public interface DeviceJpaRepository extends JpaRepository<DeviceJpaEntity, Long> {

	@Query("SELECT d FROM DeviceJpaEntity d WHERE d.deviceId = :deviceId AND d.deviceOs = :deviceOs")
	DeviceJpaEntity findByDeviceIdAndOs(@Param("deviceId") String deviceId, @Param("deviceOs") DeviceOs deviceOs);

	@Modifying
	@Query("UPDATE DeviceJpaEntity d SET d.token = :deviceToken WHERE d.deviceId = :deviceId")
	void updateDeviceToken(@Param("deviceId") String deviceId, @Param("deviceToken") String deviceToken);
}
