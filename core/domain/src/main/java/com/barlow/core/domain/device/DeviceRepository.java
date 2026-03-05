package com.barlow.core.domain.device;

import java.util.List;

public interface DeviceRepository {

	void save(DeviceRegisterCommand command);

	Device readOrNull(DeviceQuery query);

	void update(Device device);

	void deleteById(String deviceId);

	List<Device> findAllByUserNo(long userNo);
}
