package com.barlow.core.service.account.impl;

import com.barlow.core.domain.account.AccountDomainException;
import com.barlow.core.domain.device.Device;
import com.barlow.core.domain.device.DeviceQuery;
import com.barlow.core.domain.device.DeviceRepository;
import org.springframework.stereotype.Component;

@Component
public class DeviceReader {

	private final DeviceRepository deviceRepository;

	public DeviceReader(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public Device read(DeviceQuery query) {
		Device device = deviceRepository.readOrNull(query);
		if (device == null) {
			throw AccountDomainException.notFound("등록되지 않은 디바이스");
		}
		if (device.isInactive()) {
			throw AccountDomainException.forbidden("해당 디바이스는 비활성화됨");
		}
		return device;
	}
}
