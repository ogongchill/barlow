package com.barlow.core.storage;

import com.barlow.core.enumerate.DeviceOs;
import com.barlow.core.storage.support.StorageTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@StorageTest("dummy/clientVersion.json")
class ClientVersionRepositoryAdapterTest extends CoreDbContextTest {

	private final ClientVersionRepositoryAdapter adapter;

	ClientVersionRepositoryAdapterTest(ClientVersionRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("deviceOs에 따른 호환 버전을 조회하는지 확인")
	@Test
	void testRetrieveByDeviceOs() {
		assertThat(adapter.retrieveByDeviceOs(DeviceOs.ANDROID)).isNotNull();
	}
}