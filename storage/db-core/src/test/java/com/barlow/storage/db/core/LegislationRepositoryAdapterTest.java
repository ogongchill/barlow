package com.barlow.storage.db.core;

import com.barlow.core.domain.account.LegislationAccount;
import com.barlow.storage.db.CoreDbContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(statements = """
        INSERT INTO legislation_account(legislation_account_no, legislation_type, description, post_count, subscriber_count)
        VALUES 
        (1, 'GOVERNMENT', '정부', 1, 1),
        (2, 'SPEAKER', '국회의장', 1, 1),
        (3, 'HOUSE_STEERING', '국회운영위원회', 1, 1),
        (4, 'LEGISLATION_AND_JUDICIARY', '법제사법위원회', 1, 1),
        (5, 'NATIONAL_POLICY', '정무위원회', 1, 1),
        (6, 'STRATEGY_AND_FINANCE', '기획재정위원회', 1, 1),
        (7, 'EDUCATION', '교육위원회', 1, 1),
        (8, 'SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS', '과학기술정보방송통신위원회', 1, 1),
        (9, 'FOREIGN_AFFAIRS_AND_UNIFICATION', '외교통일위원회', 1, 1),
        (10, 'NATIONAL_DEFENSE', '국방위원회', 1, 1),
        (11, 'PUBLIC_ADMINISTRATION_AND_SECURITY', '행정안전위원회', 1, 1),
        (12, 'CULTURE_SPORTS_AND_TOURISM', '문화체육관광위원회', 1, 1),
        (13, 'AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES', '농림축산식품햐양수산위원회', 1, 1),
        (14, 'TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS', '산업통상자원중소벤처기업위원회', 1, 1),
        (15, 'HEALTH_AND_WELFARE', '보건복지위원회', 1, 1),
        (16, 'ENVIRONMENT_AND_LABOR', '환경노동위원회', 1, 1),
        (17, 'LAND_INFRASTRUCTURE_AND_TRANSPORT', '국토교통위원회', 1, 1),
        (18, 'INTELLIGENCE', '정보위원회', 1, 1),
        (19, 'GENDER_EQUALITY_FAMILY', '여성가족위원회', 1, 1),
        (20, 'SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS', '예산결산특별위원회', 1, 1);
        """)
class LegislationRepositoryAdapterTest extends CoreDbContextTest {

    private final LegislationRepositoryAdapter legislationRepositoryAdapter;

    public LegislationRepositoryAdapterTest(LegislationRepositoryAdapter legislationRepositoryAdapter) {
        this.legislationRepositoryAdapter = legislationRepositoryAdapter;
    }

    @Test
    void retrieveCommitteeAccount() {
        List<LegislationAccount> committeeAccount = legislationRepositoryAdapter.retrieveCommitteeAccount();
        assertThat(committeeAccount)
                .hasSize(18)
                .extracting(LegislationAccount::name) // name() 값 추출
                .contains(
                        "국회운영위원회",
                        "법제사법위원회",
                        "정무위원회",
                        "기획재정위원회",
                        "교육위원회",
                        "과학기술정보방송통신위원회",
                        "외교통일위원회",
                        "국방위원회",
                        "행정안전위원회",
                        "문화체육관광위원회",
                        "농림축산식품해양수산위원회",
                        "산업통상자원중소벤처기업위원회",
                        "보건복지위원회",
                        "환경노동위원회",
                        "국토교통위원회",
                        "정보위원회",
                        "여성가족위원회",
                        "예산결산특별위원회"
                );
    }
}