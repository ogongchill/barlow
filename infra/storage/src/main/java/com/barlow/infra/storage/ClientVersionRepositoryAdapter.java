package com.barlow.infra.storage;

import com.barlow.core.domain.version.ClientVersionPolicy;
import com.barlow.core.domain.version.ClientVersionRepository;
import com.barlow.core.enumerate.DeviceOs;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientVersionRepositoryAdapter implements ClientVersionRepository {

	private final ClientVersionJpaRepository repository;
	private final Map<DeviceOs, ClientVersionPolicy> cache = new ConcurrentHashMap<>();

	public ClientVersionRepositoryAdapter(ClientVersionJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public ClientVersionPolicy retrieveByDeviceOs(DeviceOs os) {
		return cache.computeIfAbsent(os, this::loadFromDb);
	}

	private ClientVersionPolicy loadFromDb(DeviceOs os) {
		return repository.findByDeviceOs(os).toClientVersionPolicy();
	}

	public void refresh(DeviceOs os) {
		cache.put(os, loadFromDb(os));
	}

	public void clear() {
		cache.clear();
	}
}
