package com.barlow.storage.db.core;

import com.barlow.core.domain.subscription.Subscription;
import com.barlow.storage.db.CoreDbContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(statements = """
        INSERT INTO subscribe (subscribe_no, subscriber_no, subscribe_legislation_account_no) 
        VALUES 
        (1,2,11), 
        (2,2,21), 
        (3,2,31), 
        (4,2,41), 
        (5,12,41);""")
class SubscriptionRepositoryAdapterTest extends CoreDbContextTest {

    private final SubscriptionRepositoryAdapter subscriptionRepositoryAdapter;

    public SubscriptionRepositoryAdapterTest(SubscriptionRepositoryAdapter subscriptionRepositoryAdapter) {
        this.subscriptionRepositoryAdapter = subscriptionRepositoryAdapter;
    }

    @DisplayName("memberNo를 통해 조회되는지 확인")
    @Test
    void retrieveByMemberNo() {
        List<Subscription> subscriptions = subscriptionRepositoryAdapter.retrieveByMemberNo(2L);
        assertThat(subscriptions)
                .hasSize(4)
                .contains(
                        new Subscription(1L, 2L, 11L),
                        new Subscription(2L, 2L, 21L),
                        new Subscription(3L, 2L, 31L),
                        new Subscription(4L, 2L, 41L)
                );
    }
}