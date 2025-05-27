package com.barlow.core.storage;

import com.barlow.core.enumerate.DeviceOs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientVersionJpaRepository extends JpaRepository<ClientVersionJpaEntity, Long> {

    @Query("SELECT d FROM ClientVersionJpaEntity d WHERE d.deviceOs = :deviceOs")
    ClientVersionJpaEntity findByDeviceOs(@Param("deviceOs")DeviceOs deviceOs);
}
