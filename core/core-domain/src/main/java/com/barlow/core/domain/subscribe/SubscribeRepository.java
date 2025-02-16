package com.barlow.core.domain.subscribe;

import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository {

	Subscribe retrieve(SubscribeQuery query);

	void save(Subscribe subscribe);

	void delete(Subscribe subscribe);
}
