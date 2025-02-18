package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
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

	/**
	 * FIXME : 조회되지 않는 값들은 구독 여부를 아예 알 수가 없음
	 */
	@Override
	public List<Subscribe> retrieveAll(User user) {
		return subscribeJpaRepository.findAllByMemberNo(user.getUserNo())
			.stream()
			.map(subscribeJpaEntity -> subscribeJpaEntity.toSubscribe(user))
			.toList();
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
