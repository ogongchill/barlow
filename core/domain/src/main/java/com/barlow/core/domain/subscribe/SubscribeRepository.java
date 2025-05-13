package com.barlow.core.domain.subscribe;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface SubscribeRepository {

	Subscribe retrieve(SubscribeQuery query);

	List<Subscribe> retrieveAll(User user);

	void save(Subscribe subscribe);

	void delete(Subscribe subscribe);

	void deleteAll(User user);
}
