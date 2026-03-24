# Service & Presentation Rules

## 1. core:service 레이어 구조

```
core:service/
├── {bc}/
│   ├── business/          ← @Service — 비즈니스 흐름 중계
│   │   ├── {명사}{동사}Service.java
│   │   └── {명사}Facade.java
│   └── impl/              ← @Component — 상세 구현 도구
│       ├── {명사}Reader.java
│       ├── {명사}Appender.java
│       ├── {명사}Creator.java
│       └── {명사}Manager.java
```

**UseCase/feature 단위 패키지도 `core:service`에서는 허용한다.**

```
core:service/
├── account/           ← Aggregate 단위 (범용 서비스)
├── billpost/          ← Aggregate 단위 (범용 조회)
├── home/              ← feature 단위 (홈 화면 전용)
│   ├── HomeRetrieveFacade.java
│   ├── MyHomeStatus.java    ← 크로스 Aggregate Read Model DTO (여기에만 있으면 OK)
│   └── ...
└── legislationaccount/ ← Aggregate 단위
```

`core:domain`은 Aggregate 단위만 허용. `core:service`는 feature 단위도 허용.
특정 UseCase에서만 쓰이는 서비스라면 해당 feature 패키지에 함께 둔다.

---

## 2. Business Layer 규칙 (`@Service`)

- **Implement Layer 클래스(Reader, Manager 등)만 주입받는다. `infra:*` 직접 주입 금지.**
- Business Layer 간 직접 주입 금지 → Facade로 상위에서 조합

### @Transactional — Business Layer가 트랜잭션 경계를 소유한다

**`@Transactional`은 `@Service`에만 선언한다. `@Component`(Implement Layer)에는 일절 선언하지 않는다.**

트랜잭션 경계는 Business Layer가 소유하고 관리한다. Implement Layer는 항상 Service에 의해 호출되므로, 필요한 경우 호출한 Service의 트랜잭션에 자동으로 참여(기본 `REQUIRED`)한다.

| 상황 | @Transactional 여부 |
|---|---|
| DB 쓰기를 포함하는 메서드 (단일 또는 복합 조합) | **선언** |
| 단순 읽기 위임 | **불필요** |
| `@Cacheable` 메서드 | **불필요** (`@Transactional`과 같은 메서드에 병용 금지 — 프록시 충돌) |

```java
// GOOD — DB 쓰기 포함 → @Transactional 선언
@Service
public class AccountCreateService {
    @Transactional
    public User createGuest(UserCreateCommand command, List<TermAgreement> agreements) {
        termManager.validateAgreements(agreements);
        User user = userCreator.create(command);
        termManager.saveAgreements(agreements, user);
        notificationSettingActivator.activateDefault(user);
        return user;
    }
}

// GOOD — DB 쓰기 포함 (단일 Impl 위임이라도 @Transactional 선언)
@Service
public class ReactionService {
    @Transactional
    public void react(User user, Reaction reaction) {
        reactionProcessor.react(user, reaction);
    }
}

// GOOD — 단순 읽기 위임 → @Transactional 불필요
@Service
public class LegislationAccountRetrieveService {
    public LegislationAccount retrieve(LegislationType type, User user) {
        return legislationAccountReader.read(type, user);
    }
}

// BAD — @Cacheable과 @Transactional 같은 메서드에 병용
@Service
public class BillPostCacheService {
    @Cacheable(...)
    @Transactional(readOnly = true)  // 금지
    public BillPost readBillPost(BillPostDetailQuery query) { ... }
}

// BAD — 다른 Service를 직접 주입
@Service
public class AccountCreateService {
    private final MemberRegisterService memberRegisterService; // 금지
}
```

### Facade — 여러 Service 조합

흐름이 복잡하거나 여러 Service를 조합해야 할 때 Facade를 추가한다.

```java
@Service
public class HomeRetrieveFacade {
    private final MyHomeInfoRetrieveService myHomeInfoRetrieveService;
    private final NotificationCenterItemRetrieveService notificationCenterItemRetrieveService;

    // 읽기 전용 조합 → @Transactional 불필요
    public MyHomeStatus retrieveHome(User user) {
        List<MyLegislationAccount> accounts = myHomeInfoRetrieveService.retrieve(user);
        boolean isNotificationArrived = notificationCenterItemRetrieveService.hasUnread(user);
        return new MyHomeStatus(accounts, isNotificationArrived);
    }
}
```

---

## 3. Implement Layer 규칙 (`@Component`)

- Implement Layer 클래스끼리는 서로 협력 가능 (ARCHITECTURE.md 규칙 4 예외)
- Port Interface(`core:domain`의 Repository)를 통해서만 `infra:*`와 통신
- JPA Repository, FCM SDK 등 기술 클래스 직접 사용 금지

### @Transactional 금지

**`@Component`(Implement Layer)에는 `@Transactional`을 선언하지 않는다.**

Implement Layer는 반드시 `@Service`(Business Layer)에 의해서만 호출된다.
트랜잭션은 호출한 Service가 소유하며, Implement Layer의 모든 DB 오퍼레이션은 해당 트랜잭션에 자동으로 참여한다.

만약 Implement Layer 클래스가 단독으로 트랜잭션을 필요로 하는 상황이라면, 그 클래스는 **Implement Layer가 아니라 Business Layer(`@Service`)로 승격해야 한다.**

```java
// BAD — @Component에 @Transactional
@Component
public class ReactionProcessor {
    @Transactional  // 금지
    public void react(User user, Reaction reaction) { ... }
}

// GOOD — 트랜잭션은 Service가 소유
@Service
public class ReactionService {
    @Transactional
    public void react(User user, Reaction reaction) {
        reactionProcessor.react(user, reaction);  // Processor는 Service의 Tx에 참여
    }
}

@Component
public class ReactionProcessor {
    // @Transactional 없음
    public void react(User user, Reaction reaction) { ... }
}
```

### 네이밍 컨벤션 — 역할과 허용 오퍼레이션

**접미사가 클래스의 책임을 완전히 결정한다.** 접미사와 다른 종류의 메서드를 넣지 않는다.

| 접미사 | 허용 오퍼레이션 | 예시 |
|---|---|---|
| `Reader` | **읽기 전용**. 쓰기 메서드 금지. | `LegislationAccountReader`, `BillPostReader` |
| `Appender` | 추가(insert) 전용. | `NotificationCenterAppender` |
| `Creator` | 신규 생성. | `UserCreator` |
| `Updater` | 특정 필드/상태 업데이트 전용. | `BillPostViewCountUpdater` |
| `Manager` | 읽기 + 쓰기 복합 조합. 단일 접미사로 표현 어려울 때. | `TermManager`, `LegislationAccountSubscriptionManager` |
| `Activator` | 활성화/비활성화 상태 전환. | `NotificationSettingActivator`, `SubscriptionActivator` |
| `Handler` | 특정 도메인 이벤트/시나리오 처리. | `LegislationAccountWithdrawalHandler` |
| `Processor` | 복합 처리 실행 (여러 오퍼레이션 묶음). | `ReactionProcessor` |
| `Orchestrator` | 여러 Implement 클래스를 조합하는 Impl 최상위 조율자. | `UserWithdrawalOrchestrator` |
| `Refresher` | 조건부 갱신 (현재 상태 읽고 필요 시 업데이트). | `DeviceRefresher` |

**`Reader`에 쓰기 메서드를 넣으면 안 된다.** 쓰기가 추가된 순간 `Updater`, `Manager`, `Processor` 등으로 분리하거나 이동한다.

```java
// BAD — Reader에 쓰기 메서드
@Component
public class BillPostReader {
    public void updateViewCount(String billId) { ... }  // 금지 — Reader에 쓰기 불가
}

// GOOD — 역할 분리
@Component
public class BillPostReader {
    public BillPost readBillPost(BillPostDetailQuery query) { ... }  // 읽기만
}

// 쓰기는 별도 Impl 클래스 또는 Service 메서드에서 처리
```

---

## 4. Logical CQRS

### Command Side vs Query Side

```
Command Side (Write)          Query Side (Read)
────────────────────          ──────────────────────────────────────
core:domain AR                core:domain  단일 Aggregate 투영 Read Model
  상태 변경 Repository           Repository 반환 타입

                              core:service 크로스 Aggregate 조합
                                조합 Read Model DTO
                                Query Service / Facade
```

Implement Layer(Reader, Manager)는 **Aggregate 단위를 권장**,
Business Layer(Service, Facade)는 **feature 단위를 허용**.

특정 UseCase 패키지(`home/`)의 서비스가 다른 UseCase에서도 쓰이게 되면,
그 시점에 Aggregate 패키지로 이동한다. 미리 분리하는 것은 YAGNI 위반.

---

## 5. Presentation Layer 규칙 (`app:api`)

HTTP in/out 전담. 비즈니스 로직 없음.

### Request 검증 — Validatable

Request 클래스는 `Validatable`을 구현하여 Controller 진입 시점에 즉시 검증한다.

```java
public record SignupRequest(
    String deviceOs,
    String deviceId,
    String nickname,
    Map<Long, Boolean> termAgreements
) implements Validatable {

    @Override
    public void validate() {
        if (deviceId == null || deviceId.isBlank()) {
            throw CoreApiException.badRequest("deviceId는 필수입니다.");
        }
        if (!deviceOs.matches("^(?i)(ios|android)$")) {
            throw CoreApiException.badRequest("os는 'ios' 또는 'android'만 허용됩니다.");
        }
    }
}

// Controller에서 즉시 검증
@PostMapping("/guest/signup")
public ApiResponse<LoginResponse> guestSignup(@RequestBody SignupRequest request) {
    request.validate();
    // ...
}
```

### Request → Domain Command 변환

Controller는 Request를 Domain Command/Query로 변환하여 Service에 전달한다.
Service에 Request 객체를 직접 전달하지 않는다.

```java
// GOOD
User guest = accountCreateService.createGuest(
    request.toGuestCommand(),
    request.toTermAgreements(LocalDateTime.now())
);

// BAD — Service가 Request를 알면 안 됨
accountCreateService.createGuest(request);
```

### Passport — Controller 파라미터

인증된 사용자 컨텍스트는 `Passport`로 Controller에 전달된다.

```java
@GetMapping("/me")
public ApiResponse<MyAccountResponse> getMyAccount(Passport passport) {
    User user = passport.getUser();
    // ...
}
```

---

## 6. 위반 사례 (Service/Presentation 규칙)

| 위반 패턴 | 올바른 방향 |
|---|---|
| `@Component`에 `@Transactional` 선언 | `@Transactional`은 `@Service`(Business Layer)에만. Impl에서 제거 |
| DB 쓰기를 포함한 Service 메서드에 `@Transactional` 누락 | Business Layer 메서드에 `@Transactional` 선언 |
| 단순 읽기 위임 Service 메서드에 불필요한 `@Transactional` | 트랜잭션 불필요 — 제거 |
| `@Cacheable`과 `@Transactional`을 같은 메서드에 병용 | 둘을 분리. 캐시 메서드는 `@Transactional` 없음 |
| `Reader` 클래스에 쓰기(update/save/delete) 메서드 추가 | `Updater`, `Manager`, `Processor` 등 별도 클래스로 분리 |
| Implement Layer 클래스가 단독 트랜잭션이 필요한 경우 | `@Service`로 승격 — Implement Layer가 아님 |
| Business Service에서 `infra:*` 클래스 직접 주입 | Implement Layer(Reader/Manager) 경유 |
| Business Service 간 직접 주입 | Facade로 상위 레이어에서 조합 |
| Implement Layer가 Business Layer를 알고 있음 | 역방향 참조 — 금지 |
| Controller에서 Service를 거치지 않고 Reader 직접 호출 | Business Layer(Service) 경유 |
| Controller에 Request 객체를 Service에 그대로 전달 | Request → Command 변환 후 전달 |
| Request 검증 로직이 Service 내부에 있음 | Controller 진입 시점에 `validate()` 호출 |
| UseCase 패키지를 `core:domain`에 정의 (`home/`) | `core:service`에 feature 패키지로 정의 |
| 범용 서비스를 특정 UseCase 패키지 안에 가두어 재사용 불가 | 다른 UseCase에서도 쓰이면 Aggregate 패키지로 이동 |