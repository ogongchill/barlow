package com.barlow.storage.db.core;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeQuery;
import com.barlow.core.domain.subscribe.SubscribeRepository;
import com.barlow.core.domain.subscribe.SubscribesQuery;

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
	public List<Subscribe> retrieveAll(SubscribesQuery query) {
		User user = query.user();
		List<SubscribeJpaEntity> jpaEntities = subscribeJpaRepository.findAllByMemberNo(user.getUserNo());
		List<LegislationType> actives = jpaEntities.stream()
			.map(SubscribeJpaEntity::getLegislationType)
			.toList();
		List<LegislationType> disableLegislationBodies = LegislationType.findDisableLegislationType(actives);
		return Stream.concat(
			jpaEntities.stream().map(entity -> entity.toSubscribe(user)),
			disableLegislationBodies.stream().map(disableBody -> new Subscribe(user, 1, false))
		).toList();
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
