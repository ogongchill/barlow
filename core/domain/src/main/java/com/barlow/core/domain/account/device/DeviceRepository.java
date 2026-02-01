package com.barlow.core.domain.account.device;

public interface DeviceRepository {

	void save(DeviceRegisterCommand command);

	Device readOrNull(DeviceQuery query);

	void update(Device device);

	void deleteById(String deviceId);
}
