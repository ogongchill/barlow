package com.barlow.core.domain.account;

import org.springframework.stereotype.Component;

import com.barlow.core.exception.CoreDomainExceptionType;

@Component
public class DeviceReader {

	private final DeviceRepository deviceRepository;

	public DeviceReader(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public Device read(DeviceQuery query) {
		Device device = deviceRepository.readOrNull(query);
		if (device == null) {
			throw new AccountDomainException(CoreDomainExceptionType.NOT_FOUND_EXCEPTION, "등록되지 않은 디바이스");
		}
		if (device.isInactive()) {
			throw new AccountDomainException(CoreDomainExceptionType.FORBIDDEN_EXCEPTION, "해당 디바이스는 비활성화됨");
		}
		return device;
	}
}
