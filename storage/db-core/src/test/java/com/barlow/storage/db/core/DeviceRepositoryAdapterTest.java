package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.domain.account.Device;
import com.barlow.core.domain.account.DeviceQuery;
import com.barlow.core.domain.account.DeviceRegisterCommand;
import com.barlow.core.enumerate.DeviceOs;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

import jakarta.transaction.Transactional;

@StorageTest("dummy/device.json")
class DeviceRepositoryAdapterTest extends CoreDbContextTest {

	private final DeviceRepositoryAdapter adapter;
	private final DeviceJpaRepository deviceJpaRepository;

	public DeviceRepositoryAdapterTest(DeviceRepositoryAdapter adapter, DeviceJpaRepository deviceJpaRepository) {
		this.adapter = adapter;
		this.deviceJpaRepository = deviceJpaRepository;
	}

	@DisplayName("새로운 디바이즈 정보를 받아 저장한다")
	@Test
	void save() {
		adapter.save(new DeviceRegisterCommand(
			"new_device_id", DeviceOs.IOS, "new_device_token", 100
		));

		assertThat(deviceJpaRepository.count()).isEqualTo(5);
	}

	@DisplayName("deviceId 와 deviceOs 정보로 단일 device 를 조회한다")
	@Test
	void readOrNull() {
		Device device = adapter.readOrNull(new DeviceQuery("device_id_1", DeviceOs.IOS));

		assertAll(
			() -> assertThat(device).isNotNull(),
			() -> assertThat(device.getDeviceId()).isEqualTo("device_id_1"),
			() -> assertThat(device.getDeviceToken()).isEqualTo("device_token_1")
		);
	}

	@Transactional
	@DisplayName("변경된 device 객체를 받아 디바이스 정보를 업데이트한다")
	@Test
	void update() {
		String modifiedToken = "new_token";
		Device modifiedDevice = new Device(1, "device_id_1", DeviceOs.IOS, modifiedToken, Device.Status.ACTIVE);

		adapter.update(modifiedDevice);

		assertThat(deviceJpaRepository.findByDeviceIdAndOs(modifiedDevice.getDeviceId(), DeviceOs.IOS)
			.toDevice()
			.getDeviceToken()
		).isEqualTo(modifiedToken);
	}
}