package com.barlow.core.domain.account;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeviceRefresher {

	private final DeviceReader deviceReader;
	private final DeviceRepository deviceRepository;

	public DeviceRefresher(DeviceReader deviceReader, DeviceRepository deviceRepository) {
		this.deviceReader = deviceReader;
		this.deviceRepository = deviceRepository;
	}

	@Transactional
	public Device refresh(DeviceQuery query, String deviceToken) {
		Device device = deviceReader.read(query);
		if (device.isChanged(deviceToken)) {
			Device modifiedDevice = device.modifyToken(deviceToken);
			deviceRepository.update(modifiedDevice);
		}
		return device;
	}
}
