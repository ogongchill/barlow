package com.barlow.storage.db.core;

import com.barlow.core.domain.subscription.Subscription;
import com.barlow.core.domain.subscription.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscribeJpaRepository subscribeJpaRepository;

    public SubscriptionRepositoryAdapter(SubscribeJpaRepository subscribeJpaRepository) {
        this.subscribeJpaRepository = subscribeJpaRepository;
    }

    @Override
    public List<Subscription> retrieveByMemberNo(Long memberNo) {
        return subscribeJpaRepository.findAllByMemberNo(memberNo)
                .stream()
                .map(SubscribeJpaEntity::toSubscription)
                .toList();
    }
}
