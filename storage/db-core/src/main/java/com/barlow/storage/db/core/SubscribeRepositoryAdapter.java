package com.barlow.storage.db.core;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeQuery;
import com.barlow.core.domain.subscribe.SubscribeRepository;

@Component
public class SubscribeRepositoryAdapter implements SubscribeRepository {

	private final SubscribeJpaRepository subscribeJpaRepository;

	public SubscribeRepositoryAdapter(SubscribeJpaRepository subscribeJpaRepository) {
		this.subscribeJpaRepository = subscribeJpaRepository;
	}

	@Override
	public Subscribe retrieve(SubscribeQuery query) {
		SubscribeJpaEntity subscribeJpaEntity = subscribeJpaRepository
			.findBySubscribeLegislationAccountNoAndMemberNo(query.legislationAccountNo(), query.user().getUserNo());
		if (subscribeJpaEntity == null) {
			return new Subscribe(query.user(), query.legislationAccountNo(), false);
		}
		return subscribeJpaEntity.toSubscribe(query.user());
	}

	@Override
	public void save(Subscribe subscribe) {
		subscribeJpaRepository.save(
			new SubscribeJpaEntity(subscribe.getSubscriber().getUserNo(), subscribe.getLegislationAccountNo())
		);
	}

	@Override
	public void delete(Subscribe subscribe) {
		subscribeJpaRepository.deleteBySubscribeLegislationAccountNoAndMemberNo(
			subscribe.getLegislationAccountNo(),
			subscribe.getSubscriber().getUserNo()
		);
	}
}
