# Domain Rules (core:domain 코딩 규칙)

## 1. 도메인 철학

**Domain (Enterprise Business Rules)**: 소프트웨어 시스템이 존재하지 않더라도 성립하는 불변의 비즈니스 규칙.

- `core:domain`은 Spring, JPA 등 어떤 인프라 프레임워크도 참조하지 않는다.
- 비즈니스 규칙은 Service가 아닌 **Domain 객체 내부**에 캡슐화한다 (Rich Domain Model).
- Domain 객체는 불변이다. 상태 변경은 항상 새 인스턴스를 반환한다.

---

## 2. 도메인 클래스 타입 선택 규칙

판단 흐름:

```
저장소에서 identity로 조회·저장되는가?
  ├── YES → Aggregate Root / Entity → class
  └── NO  → 데이터 전달 용도인가?
              ├── YES, 행위 없음 → record (VO/Read Model/Query/Command)
              └── NO, 비즈니스 계산 포함 → final class (VO with behavior)
```

| DDD 구성 요소 | Java 타입 | 이유 |
|---|---|---|
| **Aggregate Root** | `class` | identity 기반 동등성 + 상태 전환 행위. `final` 금지(상속 여지 유지). `record` 금지(값 기반 equals가 AR identity 파괴) |
| **Entity (비루트)** | `class` | AR 경계 내에서 AR에 의해 제어되는 식별자 보유 객체 |
| **Value Object (행위 있는 불변)** | `final class` | 값 기반 동등성 + 비즈니스 계산. `final`로 값 의미론 훼손 방지 |
| **Value Object (순수 데이터 홀더)** | `record` | 행위 없는 단순 구조체 |
| **Read Model** | `record` | 조회 전용 데이터 집계. 불변 보장 |
| **Query / Command Object** | `record` | 입력 파라미터 객체. 불변 보장 |
| **Domain Policy** | `class` | 정책 상태 보유 + 검증 행위 |
| **Port Interface / Domain Service Interface** | `interface` | 구현 기술을 모르는 포트 계약 |

### 코드 예시

```java
// Aggregate Root — class
public class Subscription {
    private final long subscriberNo;
    public Subscription activate() { ... }  // 상태 전환 → 새 인스턴스
}

// Value Object (행위 있는 불변) — final class
public final class SemanticVersion {
    private final int major, minor, patch;
    public boolean isLessThan(SemanticVersion other) { ... }  // 비즈니스 계산
    @Override public boolean equals(Object o) { ... }
}

// Value Object (순수 데이터 홀더) — record
public record ExternalPrincipal(AuthProvider authProvider, String sub) {}

// Read Model — record
public record BillPostsStatus(List<BillPost> billPosts, boolean isLastPage) {}

// Query Object — record
public record BillPostDetailQuery(String billId) {}

// Domain Policy — class
public class TermsPolicy {
    private final Set<Long> requiredTermIds;
    public void validate(List<TermAgreement> agreements) { ... }
}

// Port Interface — interface
public interface SubscriptionRepository {
    Subscription retrieve(SubscriptionQuery query);
}
```

**주의: `record`에 도메인 행위를 넣지 않는다.**

```java
// BAD — record에 도메인 행위
public record UserExternalAuth(Long userNo, List<ExternalPrincipal> principals) {
    public boolean has(AuthProvider authProvider) { ... }  // 행위가 있으면 class로
}

// GOOD — 행위 있으므로 class
public class UserExternalAuth {
    private final Long userNo;
    public boolean has(AuthProvider authProvider) { ... }
}
```

---

## 3. 불변 객체 패턴 (3가지)

### 3-1. `with` 패턴 — 컨텍스트 데이터 주입

조회 후 사용자 컨텍스트(구독 여부, 알림 여부 등)를 동적으로 채울 때 사용.

```java
// GOOD
public LegislationAccount withSubscribed(boolean subscribed) {
    return new LegislationAccount(no, type, description, postCount, subscriberCount, subscribed, isNotifiable);
}

LegislationAccount enriched = account
    .withSubscribed(true)
    .withNotifiable(false);

// BAD
account.setSubscribed(true);   // setter 금지
```

### 3-2. `activate/deactivate` 패턴 — 이진 상태 전환

```java
// Subscription, NotificationSetting 등 on/off가 명확한 도메인
public Subscription activate() {
    return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), true);
}
public Subscription deactivate() { ... }
```

### 3-3. `modify*` 패턴 — 특정 필드 갱신

```java
// Device 토큰 갱신
public Device modifyToken(String newToken) {
    return new Device(userNo, deviceId, deviceOs, newToken, status);
}
```

**공통 원칙**: setter를 전혀 사용하지 않는다.

---

## 4. Static Factory Method 패턴

생성자는 `private` 또는 `package-private`. 외부에서는 의미 있는 이름의 정적 팩토리만 사용한다.

```java
// GOOD
User.of(userNo, role)
Subscription.activate(subscriber, subscriptionInfo)
AccountDomainException.notFound()
BillPostDomainException.notFound(billId)

// BAD
new User(...)
new AccountDomainException("계정이 없음")
```

---

## 5. Rich Domain Model

비즈니스 규칙은 Service가 아닌 Domain 객체 내부에 위치한다.

```java
// BAD — 비즈니스 규칙이 Service에 있음 (Anemic Domain)
@Service
public class SubscriptionService {
    public void deactivate(Subscription subscription) {
        if (subscription.getStatus() == INACTIVE) {
            throw new IllegalStateException("이미 구독 취소 상태");
        }
        subscription.setStatus(INACTIVE); // 금지
    }
}

// GOOD — 비즈니스 규칙이 Domain 객체 내부에 있음
public class Subscription {
    public Subscription deactivate() {
        if (!this.isActive()) {
            throw SubscriptionDomainException.alreadyUnSubscribed(getLegislationType());
        }
        return new Subscription(subscriberNo, info.subscribeAccountNo(), info.subscribeAccountType(), false);
    }
}
```

---

## 6. Port Interface 정의

외부 의존성(DB, 외부 API, 캐시 등)의 인터페이스는 반드시 `core:domain`에 정의한다.
구현체(Adapter)는 `infra:*` 모듈에 위치한다.

```java
// GOOD — core:domain에 Port Interface 정의
package com.barlow.core.domain.account;

public interface UserRepository {
    User retrieve(UserQuery query);
    User create(UserRegisterCommand command);
}

// GOOD — infra:storage가 구현
package com.barlow.infra.storage;

@Component
public class UserRepositoryAdapter implements UserRepository { ... }

// BAD — core:domain이 infra를 import
import com.barlow.infra.storage.UserJpaRepository; // 금지
```

---

## 7. 패키지 구성 원칙

### 패키지 = Aggregate 단위

```
core:domain/
├── (루트)                  ← 전역 공유 프리미티브 (User, Passport)
├── account/                ← User Aggregate
│   └── term/               ← 약관 하위 패키지 (도메인 개념 단위 허용)
├── billpost/               ← BillPost Aggregate
├── device/                 ← Device Aggregate (독립 AR, ADR-001)
├── externalauth/           ← UserExternalAuth Aggregate (독립 AR, ADR-001)
├── legislationaccount/     ← LegislationAccount Aggregate
├── notificationcenter/     ← 알림 수신함 (별도 BC)
├── notificationsetting/    ← NotificationSetting Aggregate
├── reaction/               ← Reaction Aggregate
├── subscribe/              ← Subscription Aggregate (패키지명 주의: subscribe)
└── version/                ← ClientVersionPolicy Aggregate
```

### 서브패키지 허용 기준

| 서브패키지 기준 | 허용 | 예시 |
|---|---|---|
| 도메인 개념 단위 | YES | `account/term/` |
| UseCase 흐름/기능 단위 | NO | `account/create/`, `account/login/` |
| 화면(Screen) 단위 | NO | `home/` |

### 전역 공유 프리미티브 — 루트 패키지

`User`와 `Passport`는 `core.domain` 루트에 위치한다 (ADR-002).
`account/` 패키지 안에 두면 `Device`, `Subscribe`, `Reaction` 등 모든 BC가 `account`를 역방향 참조하게 된다.

```java
// GOOD — User가 루트에 있을 때
import com.barlow.core.domain.User;       // 전역 공유 개념

// BAD — User가 account/ 안에 있을 때
import com.barlow.core.domain.account.User; // subscribe, reaction 등에서 account 패키지 침범
```

**`Passport`는 `User` + 요청 컨텍스트 `Device`를 합성한 Request Context VO.**
`Passport.Device`(인라인 record)와 `core.domain.device.Device`(AR)는 다른 객체다:
- `Passport.Device` = 요청 세션 컨텍스트 (deviceId, osVersion, DeviceOs)
- `core.domain.device.Device` = 퍼시스턴스 AR (deviceToken, Status 등)

---

## 8. Command/Query 소속 원칙

**모든 Command/Query는 `core:domain`에 정의한다.**

`core:service`에 두면 `core:domain`(Repository 인터페이스)이 `core:service`를 역방향 참조해야 한다.

```java
// BAD — Command가 core:service에 있는 경우
interface DeviceRepository {              // core:domain
    Device read(LoginCommand command);    // core:service를 역참조 → 금지
}
```

| 종류 | 위치 |
|---|---|
| Repository 인터페이스 | `core:domain` |
| Repository Command/Query | `core:domain` |
| UseCase Command/Query | `core:domain` |
| UseCase 인터페이스 | `core:service` |

---

## 9. AR 간 참조 원칙 — ID-only

AR 간 객체 직접 참조 금지. `long` ID로만 참조한다.

```java
// GOOD
public class Subscription {
    private final long subscriberNo;  // User 객체 아님, userNo만
}

public class Device {
    private final long userNo;  // User 객체 아님, userNo만
}

// BAD
public class Subscription {
    private final User subscriber;  // 직접 참조 금지
}
```

User 객체가 필요한 경우 `core:service` 레이어가 `UserRepository`로 직접 조회한다.

---

## 10. CQRS — Read Model 위치 결정

| 유형 | 위치 | 판단 기준 |
|---|---|---|
| 단일 Aggregate 투영 | `core:domain` 해당 Aggregate 패키지 | Repository 반환 타입으로 사용 |
| 크로스 Aggregate 조합 | `core:service` 해당 feature 패키지 | 여러 Repository 결과 조합 |

```java
// 단일 Aggregate 투영 — core:domain/billpost/ 에 위치
// BillPostRepository.findTodayPosts() 반환 타입
record TodayBillPostThumbnail(String billId, String billName, LocalDate createdAt) {}

// 크로스 Aggregate 조합 — core:service/home/ 에 위치
// HomeRetrieveFacade가 legislationaccount + notificationsetting 조합
record MyHomeStatus(List<MyLegislationAccount> accounts, boolean isNotificationArrived) {}
```

---

## 11. infra:storage 변환 규칙

JPA Entity는 `infra:storage` 패키지 내부에만 존재한다.

- `core:domain` → `infra:storage`: `XxxJpaEntity.from(domainObject)`
- `infra:storage` → `core:domain`: `entity.toDomain()`

```java
@Component
public class UserRepositoryAdapter implements UserRepository {

    @Override
    public User retrieve(UserQuery query) {
        return userJpaRepository.findByNo(query.userNo()).toDomain();
    }

    @Override
    public User create(UserRegisterCommand command) {
        return userJpaRepository.save(UserJpaEntity.from(command)).toDomain();
    }
}
```

| 종류 | 네이밍 예시 |
|---|---|
| Port Interface (core:domain) | `UserRepository` |
| Adapter 구현체 (infra:storage) | `UserRepositoryAdapter` |
| Spring Data JPA | `UserRepositoryJpaRepository` |
| JPA Entity | `UserJpaEntity` |

---

## 12. 위반 사례 (도메인 규칙)

| 위반 패턴 | 올바른 방향 |
|---|---|
| `core:domain`에 `@Entity`, `@Service`, `@Transactional` 추가 | 순수 POJO 유지 |
| Domain 객체에 setter로 상태 변경 | 새 인스턴스 반환 (`with/activate/deactivate/modify*`) |
| `record`에 도메인 행위(비즈니스 검증, 계산) 추가 | `final class` 또는 `class`로 전환 |
| AR 간 객체 직접 참조 | ID-only 참조 (`long` 타입) |
| Command/Query를 `core:service`에 정의 | `core:domain`에 정의 (역방향 의존 원천 차단) |
| 비즈니스 규칙을 Service에 위치 | Domain 객체 내부에 캡슐화 |
| 도메인 패키지를 UseCase 기준으로 분류 | Aggregate 단위로 분류 |
| 화면 단위 패키지를 `core:domain`에 정의 (`home/`) | `core:domain`은 Aggregate 단위만 |
| 크로스 Aggregate Read Model을 `core:domain`에 정의 | `core:service` feature 패키지에 정의 |
| `UserExternalAuth`를 record로 선언 (has() 행위 있음) | class로 선언 |