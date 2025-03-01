package com.barlow.core.domain.account;

public interface DeviceRepository {

	void save(DeviceRegisterCommand command);

	Device read(DeviceQuery query);

	void update(Device device);
}
