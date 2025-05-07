package com.barlow.core.domain.account;

public interface DeviceRepository {

	void save(DeviceRegisterCommand command);

	Device readOrNull(DeviceQuery query);

	void update(Device device);
}
