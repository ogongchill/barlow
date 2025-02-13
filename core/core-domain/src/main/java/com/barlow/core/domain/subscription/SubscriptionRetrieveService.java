package com.barlow.core.domain.subscription;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionRetrieveService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionRetrieveService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public MemberSubscriptions retrieveSubscriptionsByMemberNo(Long memberNo) {
        return new MemberSubscriptions(subscriptionRepository.retrieveByMemberNo(memberNo));
    }
}
