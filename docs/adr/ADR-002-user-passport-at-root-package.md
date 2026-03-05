# ADR-002: User / Passport를 루트 도메인 패키지에 유지

- **상태**: 승인됨
- **결정일**: 2026-03-04
- **관련 모듈**: `core:domain`

---

## 맥락

`User`는 `core:domain`의 애그리거트 루트이면서, 동시에 시스템 전체에서 "누구의 요청인가"를
표현하는 공유 도메인 타입으로 사용된다.

`User`의 생명주기 관리(생성/프로모션/탈퇴)에 관한 커맨드, 쿼리, 예외, Repository는
`account/` 패키지에 위치하지만, `User` 클래스 자체와 `Passport`는
`com.barlow.core.domain` 루트 패키지에 위치한다.

이 분리 구조가 의도적인지, 아니면 정리가 필요한 상태인지 명확히 할 필요가 있다.

---

## 결정

**`User`와 `Passport`를 `com.barlow.core.domain` 루트 패키지에 유지한다.**

`account/` 패키지로 이동하지 않는다.

---

## 근거

### 1. User는 특정 애그리거트에 귀속되지 않는 공유 도메인 원시 타입이다

`User`는 시스템 전체 애그리거트의 Repository 시그니처에서 공통으로 사용된다.

```java
SubscribeRepository.retrieveAll(User user)
ReactionRepository.react(User user, Reaction reaction)
NotificationSettingRepository.retrieveNotificationSettings(User user)
LegislationAccountRepository.retrieve(User user, LegislationType legislationType)
DeviceRepository.findAllByUserNo(User user)
```

이것은 `User`가 "계정 도메인의 AR"이기 이전에, **"누구의 요청인가"를 표현하는 전역 식별 타입**임을 의미한다.

### 2. account/ 패키지로 이동하면 잘못된 의존성이 발생한다

`User`를 `account/`로 이동하면 `reaction/`, `subscribe/`, `notificationsetting/` 등
계정과 무관한 모든 애그리거트가 `account/` 패키지에 의존하게 된다.

```java
// User를 account/로 이동했을 때 발생하는 부자연스러운 의존
import com.barlow.core.domain.account.User;  // reaction이 account를 참조
import com.barlow.core.domain.account.User;  // subscribe가 account를 참조
import com.barlow.core.domain.account.User;  // notificationsetting이 account를 참조
```

패키지는 응집도 높은 개념의 경계를 표현해야 한다.
`User`를 `account/`에 두는 것은 "이 타입은 account 도메인 소유"라는 의미를 내포하지만,
실제로는 시스템 전체가 공유하는 타입이다.

### 3. account/ 패키지의 역할은 User 생명주기 관리다

`account/` 패키지는 `User`를 소유하는 것이 아니라,
`User`의 **생명주기(생성·프로모션·탈퇴)를 관리하는 커맨드/쿼리/예외/Repository**를 담는다.

```
account/
├── UserRepository.java             ← User 생명주기 영속성 Port
├── UserWithdrawalProcessor.java    ← 탈퇴 처리 Port
├── AccountDomainException.java     ← 계정 도메인 예외
├── UserCreateCommand.java          ← 생성 커맨드
├── GuestToMemberCommand.java       ← 프로모션 커맨드
└── term/                           ← 약관 서브도메인
```

`User` 클래스 자체는 여기에 속하지 않는다. `User`는 `account/`가 관리하는 **대상**이지,
`account/` 패키지가 소유하는 개념이 아니다.

### 4. Passport는 계층 경계 VO다

`Passport`는 HTTP 인증 컨텍스트(`User` + 기기 정보)를 도메인 타입으로 변환한 VO다.
특정 애그리거트의 생명주기와 무관하며, 인증 레이어(`infra:auth`)와 도메인 레이어 사이의
경계에서 사용된다. `account/` 패키지에 귀속시킬 이유가 없다.

---

## 결과

- `User`, `Passport`는 `com.barlow.core.domain` 루트 패키지에 위치한다.
- `account/` 패키지는 User **생명주기 관리**에 관한 커맨드/쿼리/예외/Repository만 보유한다.
- 다른 애그리거트(`reaction/`, `subscribe/` 등)는 루트의 `User`를 직접 import하며,
  이는 의도된 구조다.

---

## 고려했으나 채택하지 않은 대안

### account/ 패키지로 이동

`User`가 계정 도메인의 AR이므로 `account/`에 두는 것이 DDD 원칙에 부합한다는 주장.
그러나 전 시스템이 공유하는 타입을 특정 도메인 패키지에 귀속시키면
오히려 불필요한 패키지 간 의존성이 발생한다. 기각.

### shared/ 또는 identity/ 패키지 신설

`User`와 `Passport`를 위한 별도 공유 패키지를 만드는 방안.
현재 공유 타입이 `User`, `Passport` 두 개뿐이므로 패키지 신설의 실익이 없다.
루트 패키지 자체가 공유 네임스페이스 역할을 충분히 수행한다. 기각.