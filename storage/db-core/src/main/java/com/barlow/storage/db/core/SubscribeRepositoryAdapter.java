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
			return new Subscribe(query.user(), "", false); // accountNo 랑 ordinal 이랑 비교해서 찾기
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
			disableLegislationBodies.stream().map(disableBody -> new Subscribe(
				user,
				disableBody.getValue(),
				false
			))
		).toList();
	}

	@Override
	public void save(Subscribe subscribe) {
		subscribeJpaRepository.save(
			new SubscribeJpaEntity(
				1L, // ordinal + 1 로 대체하자
				LegislationType.valueOf(subscribe.getLegislationAccountType()),
				subscribe.getSubscriber().getUserNo()
			)
		);
	}

	@Override
	public void delete(Subscribe subscribe) {
		subscribeJpaRepository.deleteByLegislationTypeAndMemberNo(
			LegislationType.valueOf(subscribe.getLegislationAccountType()),
			subscribe.getSubscriber().getUserNo()
		);
	}
}
