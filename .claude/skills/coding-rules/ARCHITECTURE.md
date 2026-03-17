# Architecture Rules

## 1. 아키텍처 철학

**"상세 구현 로직은 잘 모르더라도 비즈니스의 흐름은 이해 가능한 로직이어야 한다."**

비즈니스 로직은 구현 기술을 모르는 상태로 작성된다. Service가 JPA를 쓰는지, FCM을 쓰는지 알 필요가 없다.
모듈화는 요구사항이 생길 때마다 **기술이 아닌 개념** 단위로 확장한다.

---

## 2. 레이어 구조

```
[Presentation]   app:api / app:batch
                 Controller, Request/Response
      ↓
[Business]       core:service  (business/ 패키지)
                 Service, Facade
      ↓
[Implement]      core:service  (impl/ 패키지)
                 Reader, Manager, Creator, Appender
      ↓
[Data Access]    infra:*
                 기술 의존성 격리, Port Interface 구현

[Shared Models]  core:domain
                 AR, VO, Enum, Port Interface (Repository)
```

---

## 3. 모듈 구조

```
core/
├── domain/     AR, VO, Enum, Port Interface, Domain Exception
└── service/    Business Layer + Implement Layer

infra/
├── storage/    JPA 어댑터 (MySQL)
├── auth/       JWT/OIDC 어댑터
├── notification/  FCM 어댑터
├── post-view/  조회수 캐시 어댑터
└── clients/knal-api/  국회 외부 API 클라이언트

support/
├── alert/      Slack 알럿
├── logging/    로깅 (Sentry)
└── monitoring/ 모니터링

app/
├── api/        API 서버 (bootJar)
└── batch/      배치 서버 (bootJar)

tests/
└── api-docs/   REST Docs 테스트 지원
```

### 의존성 방향

```
app:api / app:batch
    ↓  implementation
core:service
    ↓  implementation
core:domain  ←  implementation  ←  infra:*
```

- `infra:*`는 `core:domain`의 Port Interface를 구현한다.
- `app:api`는 `infra:storage`를 `runtimeOnly`로만 의존한다 — 컴파일 타임에 JPA 기술에 접근 불가.

---

## 4. 레이어 4대 규칙

**규칙 1 — 순방향 참조만 허용 (위 → 아래).**

**규칙 2 — 역방향 참조 금지.**
Implement Layer의 `Reader`가 Business Layer의 `Service`를 알면 안 된다.

**규칙 3 — 레이어 건너뜀 금지.**
Business Layer(Service)가 Data Access Layer(JPA Repository, JPA Entity)를 직접 참조하면 안 된다.
Business Layer는 Implement Layer를 통해서만 데이터에 접근한다.

```java
// BAD — Business가 Data Access 직접 참조
@Service
public class AccountCreateService {
    private final UserJpaRepository userJpaRepository; // 금지
}

// GOOD — Business는 Implement Layer만 안다
@Service
public class AccountCreateService {
    private final UserCreator userCreator;
    private final TermManager termManager;
    private final NotificationSettingActivator notificationSettingActivator;
}
```

**규칙 4 — 동일 레이어 간 참조 금지. 단, Implement Layer는 예외.**
Implement Layer 클래스들은 서로 협력하여 재사용성 높은 도구를 만들 수 있다.

```java
// GOOD — Implement 간 협력 허용
@Component
public class LegislationAccountReader {
    private final LegislationAccountRepository legislationAccountRepository;
    private final SubscriptionReader subscriptionReader;         // Implement 협력 OK
    private final NotificationSettingReader notificationSettingReader;
}

```

---

## 5. 모듈 확장 원칙

**새 요구사항 → 새 모듈 추가. 기존 모듈에 기술 추가 금지.**

```
// 예: 새 외부 API 연동 요구사항
infra/
└── new-external-api/   ← 새 모듈. 기존 모듈에 추가 금지.
```

**`implementation`을 최우선 사용한다.**

```gradle
// GOOD — 기술이 상위 모듈에 전파되지 않음
dependencies {
    implementation project(":infra:storage")
}

// BAD — api 키워드 사용 시 하위 의존성 모두 전파됨
dependencies {
    api project(":infra:storage")
}
```