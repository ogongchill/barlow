package com.barlow.infra.storage;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.subscribe.Subscription;
import com.barlow.core.domain.subscribe.SubscriptionQuery;
import com.barlow.core.domain.subscribe.SubscriptionRepository;
import com.barlow.core.enumerate.LegislationType;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

	private final SubscriptionJpaRepository subscribeJpaRepository;

	public SubscriptionRepositoryAdapter(SubscriptionJpaRepository subscribeJpaRepository) {
		this.subscribeJpaRepository = subscribeJpaRepository;
	}

	@Override
	public Subscription retrieve(SubscriptionQuery query) {
		User user = query.user();
		LegislationType legislationType = query.legislationType();
		SubscriptionJpaEntity subscribeJpaEntity = subscribeJpaRepository
			.findBySubscribeLegislationAccountNoAndMemberNo(legislationType.getLegislationNo(), user.getUserNo());
		if (subscribeJpaEntity == null) {
			return new Subscription(user.getUserNo(), legislationType.getLegislationNo(), legislationType, false);
		}
		return subscribeJpaEntity.toSubscription();
	}

	@Override
	public List<Subscription> retrieveAll(User user) {
		List<SubscriptionJpaEntity> jpaEntities = subscribeJpaRepository.findAllByMemberNo(user.getUserNo());
		List<LegislationType> actives = jpaEntities.stream().map(SubscriptionJpaEntity::getLegislationType).toList();
		List<LegislationType> disableLegislationBodies = LegislationType.findDisableLegislationType(actives);
		return Stream.concat(
			jpaEntities.stream().map(SubscriptionJpaEntity::toSubscription),
			disableLegislationBodies.stream()
				.map(disableBody -> new Subscription(user.getUserNo(), disableBody.getLegislationNo(), disableBody,
					false)))
			.toList();
	}

	@Override
	public void save(Subscription subscription) {
		subscribeJpaRepository.save(
			new SubscriptionJpaEntity(
				subscription.getSubscribeAccountNo(), subscription.getLegislationType(),
				subscription.getSubscriberNo()));
	}

	@Override
	public void delete(Subscription subscription) {
		subscribeJpaRepository.deleteBySubscribeLegislationAccountNoAndMemberNo(
			subscription.getSubscribeAccountNo(), subscription.getSubscriberNo());
	}

	@Override
	public void deleteAll(User user) {
		subscribeJpaRepository.deleteAllByMemberNo(user.getUserNo());
	}
}
