package com.barlow.infra.storage;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.account.device.Device;
import com.barlow.core.domain.account.device.DeviceQuery;
import com.barlow.core.domain.account.device.DeviceRegisterCommand;
import com.barlow.core.domain.account.device.DeviceRepository;

import java.util.List;

@Component
public class DeviceRepositoryAdapter implements DeviceRepository {

	private final DeviceJpaRepository deviceJpaRepository;

	public DeviceRepositoryAdapter(DeviceJpaRepository deviceJpaRepository) {
		this.deviceJpaRepository = deviceJpaRepository;
	}

	@Override
	public void save(DeviceRegisterCommand command) {
		deviceJpaRepository
			.save(new DeviceJpaEntity(command.deviceId(), command.os(), command.deviceToken(), command.userNo()));
	}

	@Override
	public Device readOrNull(DeviceQuery query) {
		DeviceJpaEntity deviceJpaEntity = deviceJpaRepository.findByDeviceIdAndOs(query.deviceId(), query.deviceOs());
		if (deviceJpaEntity == null) {
			return null;
		}
		return deviceJpaEntity.toDevice();
	}

	@Override
	public void update(Device device) {
		deviceJpaRepository.updateDeviceToken(device.getDeviceId(), device.getDeviceToken());
	}

	@Override
	public void deleteById(String deviceId) {
		deviceJpaRepository.deleteByDeviceId(deviceId);
	}

	@Override
	public List<Device> findAllByUserNo(long userNo) {
		return deviceJpaRepository.findAllByMemberNo(userNo).stream().map(DeviceJpaEntity::toDevice).toList();
	}
}
