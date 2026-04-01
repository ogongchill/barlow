# core:domain 모듈

비즈니스 규칙과 도메인 모델을 담는 순수 Java 모듈.  
Spring 어노테이션 없이 도메인 로직만 존재하며, 모든 상위 모듈이 이 모듈에 의존합니다.

**의존 방향**: `app → core:service → core:domain ← infra:*`

---

## 설계 원칙

- **불변성** — AR은 상태 변경 시 새 인스턴스를 반환 (`with*` / `activate()` / `deactivate()` 패턴)
- **순수 POJO** — Spring 어노테이션, setter 금지
- **AR 간 참조** — ID(`long`)만 허용, 다른 AR 객체 직접 참조 금지
- **예외** — `CoreDomainException` 하위 + static factory 메서드
- **VO** — `record` 타입으로 정의, 불변성 자동 보장
- **Repository** — 인터페이스만 정의, 구현은 `infra:storage`

---

## BC 구성

| BC | 담당 도메인 개념 |
|----|----------------|
| `account` | 사용자 계정, 회원가입, 역할(Guest/Member/Admin) |
| `account.term` | 이용약관, 약관 동의 검증 |
| `billpost` | 법안 게시물, 제안자, 오늘의 법안 |
| `device` | 사용자 기기, 푸시 토큰 |
| `reaction` | 법안·게시물 반응 |
| `subscribe` | 입법기관 구독 |
| `externalauth` | 외부 인증 제공자 (OAuth) |
| `notificationsetting` | 알림 설정 |
| `notificationcenter` | 알림 센터 아이템 |
| `legislationaccount` | 입법기관 계정 정보 |
| `version` | 클라이언트 버전 관리 |

---

## 패키지 구조

```
com.barlow.core/
├── domain/
│   ├── User.java              # 핵심 사용자 엔티티
│   ├── Passport.java          # 인증 정보 (User + Device 조합)
│   ├── account/
│   ├── billpost/
│   ├── device/
│   ├── reaction/
│   ├── subscribe/
│   ├── externalauth/
│   ├── notificationsetting/
│   ├── notificationcenter/
│   ├── legislationaccount/
│   └── version/
├── exception/                 # CoreDomainException 계층
├── enumerate/                 # 공유 열거형
└── support/                   # SortKey 등 공통 VO
```

각 BC 패키지는 AR · VO · Repository 인터페이스 · Query/Command DTO · Policy · DomainException을 포함합니다.

---

## 주요 패턴

### Aggregate Root

상태 변경은 새 인스턴스를 반환합니다.

```
// Subscription
Subscription activate()
Subscription deactivate()

// LegislationAccount
LegislationAccount withSubscribed(boolean subscribed)
```

### Value Object

`record`로 정의합니다.

```
record BillProposer(String proposerCode, String proposerName, PartyName partyName, ...)
record Term(Long id, String title, boolean required, LocalDateTime effectiveAt, ...)
```

### Policy

비즈니스 규칙을 캡슐화합니다.

| Policy | 역할 |
|--------|------|
| `TermsPolicy` | 필수 약관 동의 여부 검증 |
| `ClientVersionPolicy` | 클라이언트 버전 상태 평가 (최신 · 업데이트 권장 · 강제 업데이트) |

### Query / Command

Repository 메서드 인자는 조회 조건(`*Query`) 또는 명령(`*Command`) record로 분리합니다.

---

## 예외 체계

```
CoreDomainException (abstract)
├── AccountDomainException
├── RegistrationException
├── BillPostDomainException
├── ReactionDomainException
├── SubscriptionDomainException
├── NotificationSettingDomainException
└── ClientVersionException
```

- **레벨** — `BUSINESS` (비즈니스 규칙 위반) / `IMPLEMENTATION` (시스템 오류)
- **코드** — E400 · E401 · E403 · E404 · E409 · E500
- 생성은 static factory 메서드만 사용 (`AccountDomainException.accountNotFound()`)

---

## 열거형 (enumerate)

| 열거형 | 설명 |
|--------|------|
| `LegislationType` | 17개 위원회 + 정부 · 국회의장 · 특위 |
| `NotificationTopic` | 위원회별 · 진행상태별 · 상호작용 알림 주제 |
| `ProgressStatus` | 법안 진행 상태 (접수 → 공포) |
| `ReactionType` / `ReactionTarget` | 반응 유형 · 대상 |
| `AuthProvider` | 외부 인증 제공자 |
| `DeviceOs` | iOS · Android |

---

## 의존 모듈

```gradle
compileOnly("org.springframework:spring-context")
compileOnly("org.springframework:spring-tx")
```

Spring 의존성은 `compileOnly`로만 참조해 도메인 순수성을 유지합니다.
