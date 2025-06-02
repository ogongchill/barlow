package com.barlow.core.storage;

import com.barlow.core.domain.version.AvailableClientVersion;
import com.barlow.core.domain.version.ClientVersionRepository;
import com.barlow.core.enumerate.DeviceOs;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientVersionRepositoryAdapter implements ClientVersionRepository {

	private final ClientVersionJpaRepository repository;
	private final Map<DeviceOs, AvailableClientVersion> cache = new ConcurrentHashMap<>();

	public ClientVersionRepositoryAdapter(ClientVersionJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public AvailableClientVersion retrieveByDeviceOs(DeviceOs os) {
		return cache.computeIfAbsent(os, this::loadFromDb);
	}

	private AvailableClientVersion loadFromDb(DeviceOs os) {
		return repository.findByDeviceOs(os).toAvailableClientVersion();
	}

	public void refresh(DeviceOs os) {
		cache.put(os, loadFromDb(os));
	}

	public void clear() {
		cache.clear();
	}
}
