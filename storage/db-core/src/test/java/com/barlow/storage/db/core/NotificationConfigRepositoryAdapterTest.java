package com.barlow.storage.db.core;

import com.barlow.core.domain.notification.NotificationConfig;
import com.barlow.storage.db.CoreDbContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(statements = """
    INSERT INTO notification_config(notification_config_no, topic, enable, member_no)
    VALUES 
    (1,'RECEIPT', false, 1),
    (2,'SUBMISSION_PLENARY_SESSION', true , 1),
    (3,'RESOLUTION_PLENARY_SESSION', true , 1),
    (4,'RECONSIDERATION_GOVERNMENT', true , 1),
    (5,'PROMULGATION', false , 1),
    (6,'HOUSE_STEERING', true , 1),
    (7,'LEGISLATION_AND_JUDICIARY', true , 1),
    (8,'NATIONAL_POLICY', true , 1),
    (9,'STRATEGY_AND_FINANCE', true , 1),
    (10,'EDUCATION', false , 1),
    (11,'SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS', true , 1),
    (12,'RECEIPT', true , 2),
    (13,'SUBMISSION_PLENARY_SESSION', true , 2),
    (14,'RESOLUTION_PLENARY_SESSION', true , 2),
    (15,'RECONSIDERATION_GOVERNMENT', true , 2),
    (16,'PROMULGATION', true , 2),
    (17,'HOUSE_STEERING', true , 2),
    (18,'LEGISLATION_AND_JUDICIARY', true , 2),
    (19,'NATIONAL_POLICY', true , 2),
    (20,'STRATEGY_AND_FINANCE', true , 2),
    (21,'EDUCATION', true , 2),
    (22,'SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS', true , 2);""")
class NotificationConfigRepositoryAdapterTest extends CoreDbContextTest {

    private final NotificationConfigRepositoryAdapter notificationConfigRepositoryAdapter;

    public NotificationConfigRepositoryAdapterTest(NotificationConfigRepositoryAdapter notificationConfigRepositoryAdapter) {
        this.notificationConfigRepositoryAdapter = notificationConfigRepositoryAdapter;
    }

    @DisplayName("memberNo를 통해 알림설정이 조회되는지 확인")
    @Test
    void testRetrieveByMemberNo() {
        Long targetMemberNo = 1L;
        List<NotificationConfig> notificationConfigs = notificationConfigRepositoryAdapter.retrieveByMemberNo(targetMemberNo);
        assertThat(notificationConfigs)
                .hasSize(11)
                .extracting(NotificationConfig::memberNo)
                .allMatch(memberNo -> memberNo.equals(targetMemberNo));
    }
}