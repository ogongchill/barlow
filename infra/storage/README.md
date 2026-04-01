# infra:storage 모듈

JPA 기반 데이터 영속성 계층 모듈. `core:domain`에 정의된 Repository 인터페이스를 구현합니다.

---

## 패키지 구조

```
com.barlow.infra.storage/
├── config/                  # DataSource(HikariCP) · JPA 설정
└── (BC별 클래스 평면 배치)
    ├── *JpaEntity           # JPA 엔티티
    ├── *JpaRepository       # Spring Data JPA 인터페이스
    └── *RepositoryAdapter   # core:domain Repository 구현체 (@Component)
```

모든 엔티티는 `BaseTimeJpaEntity`(`createdAt`, `updatedAt`)를 상속합니다.

---

## Repository 구현 패턴

`core:domain`의 Repository 인터페이스를 Adapter가 구현하고, 내부에서 JpaRepository에 위임합니다.

```
core:domain Repository (인터페이스)
    ↑ implements
*RepositoryAdapter  (@Component)
    ↑ delegates
*JpaRepository  (Spring Data JPA)
    ↑ manages
*JpaEntity
```

쿼리는 목적에 따라 아래 방식을 사용합니다.

| 방식 | 사용 시점 |
|------|---------|
| Method Naming | 단순 조건 조회 |
| JPQL `@Query` | JOIN · 벌크 업데이트·삭제 |
| `JpaSpecificationExecutor` | 동적 다중 필터 조합 (법안 목록 등) |

---

## 주요 엔티티

| 엔티티 | 테이블 주요 컬럼 |
|--------|---------------|
| `UserJpaEntity` | no, nickname, role |
| `DeviceJpaEntity` | memberNo, deviceId, deviceOs, token, status |
| `ExternalAuthJpaEntity` | memberNo, provider, sub |
| `TermJpaEntity` | type, title, version, required, effectiveAt |
| `TermAgreementJpaEntity` | memberNo, termNo, agreed |
| `BillPostJpaEntity` | billId, billName, legislationType, progressStatus, viewCount, @Embedded PreAnnouncementInfo |
| `BillProposerJpaEntity` | proposeBillId, proposerCode, partyName |
| `LegislationAccountJpaEntity` | legislationType, postCount, subscriberCount |
| `SubscriptionJpaEntity` | memberNo, subscribeLegislationAccountNo, legislationType |
| `NotificationConfigJpaEntity` | memberNo, topic, enable |
| `NotificationCenterItemJpaEntity` | memberNo, billId, notificationTopic, title, body |
| `ReactionJpaEntity` | memberNo, targetId, targetType, type |
| `ClientVersionJpaEntity` | deviceOs, minimumSupported, latest |

---

## 설정 (storage-core.yml)

```yaml
spring.jpa:
  open-in-view: false
  hibernate.ddl-auto: validate
  properties.hibernate.default_batch_fetch_size: 100
```

### 프로파일별 DataSource

| 프로파일 | DB | 비고 |
|---------|-----|------|
| `local` · `test` | H2 인메모리 (MySQL 호환 모드) | ddl-auto: create |
| `dev` · `staging` · `live` | MySQL | 환경변수로 접속 정보 주입 |

MySQL 환경변수: `STORAGE_DATABASE_CORE_DB_URL`, `STORAGE_DATABASE_CORE_DB_USERNAME`, `STORAGE_DATABASE_CORE_DB_PASSWORD`

---

## 의존 모듈

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'com.mysql:mysql-connector-j'
runtimeOnly 'com.h2database:h2'
compileOnly project(':core:domain')
```
