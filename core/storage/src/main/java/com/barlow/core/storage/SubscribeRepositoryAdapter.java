package com.barlow.core.storage;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeQuery;
import com.barlow.core.domain.subscribe.SubscribeRepository;
import com.barlow.core.enumerate.LegislationType;

@Component
public class SubscribeRepositoryAdapter implements SubscribeRepository {

	private final SubscribeJpaRepository subscribeJpaRepository;

	public SubscribeRepositoryAdapter(SubscribeJpaRepository subscribeJpaRepository) {
		this.subscribeJpaRepository = subscribeJpaRepository;
	}

	@Override
	public Subscribe retrieve(SubscribeQuery query) {
		User user = query.user();
		LegislationType legislationType = query.legislationType();
		SubscribeJpaEntity subscribeJpaEntity = subscribeJpaRepository
			.findBySubscribeLegislationAccountNoAndMemberNo(legislationType.getLegislationNo(), user.getUserNo());
		if (subscribeJpaEntity == null) {
			return new Subscribe(
				user,
				legislationType.getLegislationNo(),
				legislationType,
				false
			);
		}
		return subscribeJpaEntity.toSubscribe(user);
	}

	@Override
	public List<Subscribe> retrieveAll(User user) {
		List<SubscribeJpaEntity> jpaEntities = subscribeJpaRepository.findAllByMemberNo(user.getUserNo());
		List<LegislationType> actives = jpaEntities.stream()
			.map(SubscribeJpaEntity::getLegislationType)
			.toList();
		List<LegislationType> disableLegislationBodies = LegislationType.findDisableLegislationType(actives);
		return Stream.concat(
			jpaEntities.stream().map(entity -> entity.toSubscribe(user)),
			disableLegislationBodies.stream().map(disableBody -> new Subscribe(
				user,
				disableBody.getLegislationNo(),
				disableBody,
				false
			))
		).toList();
	}

	@Override
	public void save(Subscribe subscribe) {
		subscribeJpaRepository.save(
			new SubscribeJpaEntity(
				subscribe.getSubscribeAccountNo(),
				subscribe.getLegislationType(),
				subscribe.getSubscriber().getUserNo()
			)
		);
	}

	@Override
	public void delete(Subscribe subscribe) {
		subscribeJpaRepository.deleteBySubscribeLegislationAccountNoAndMemberNo(
			subscribe.getSubscribeAccountNo(),
			subscribe.getSubscriber().getUserNo()
		);
	}

	@Override
	public void deleteAll(User user) {
		subscribeJpaRepository.deleteAllByMemberNo(user.getUserNo());
	}
}
