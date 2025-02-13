package com.barlow.core.domain.subscription;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository {

    List<Subscription> retrieveByMemberNo(Long memberNo);
}
