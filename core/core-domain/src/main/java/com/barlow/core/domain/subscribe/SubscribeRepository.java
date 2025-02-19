package com.barlow.core.domain.subscribe;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository {

	Subscribe retrieve(SubscribeQuery query);

	List<Subscribe> retrieveAll(SubscribesQuery query);

	void save(Subscribe subscribe);

	void delete(Subscribe subscribe);
}
