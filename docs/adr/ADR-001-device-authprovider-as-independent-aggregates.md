# ADR-001: Device / UserAuthProvider를 독립 애그리거트로 유지

- **상태**: 승인됨
- **결정일**: 2026-03-04
- **관련 모듈**: `core:domain`, `core:service`, `infra:storage`

---

## 맥락

`Device`와 `UserAuthProvider`는 `User` 없이 존재할 수 없고, `User` 삭제 시 함께 삭제된다.
DDD 원칙의 엄격한 해석에 따르면 이 둘은 `User` 애그리거트의 하위 엔티티로 묶여야 하며,
`UserRepository`를 통해서만 접근되어야 한다.

그러나 현재 코드베이스에서는 `device/`, `authprovider/` 독립 패키지에 각각
`DeviceRepository`, `AuthProviderRepository`를 두고 독립 애그리거트처럼 취급한다.

---

## 결정

**`Device`와 `UserAuthProvider`를 독립 애그리거트(별도 패키지, 별도 Repository)로 유지한다.**

---

## 근거

### 1. 접근 패턴이 `User`와 독립적이다

실제 유스케이스에서 `Device`와 `UserAuthProvider`는 `User` 객체 없이 직접 조회·갱신된다.

- Device 토큰 갱신: `deviceRepository.readOrNull(query)` → `modifyToken()` → `deviceRepository.update()`
- AuthProvider 조회: `authProviderRepository.retrieveByUser(query)` (userNo로만 조회)

`User` AR을 통해 접근하도록 강제하면 매 작업마다 불필요한 `User` 로드가 선행된다.

### 2. 트랜잭션 경계가 다르다

`Device` 토큰 갱신, `AuthProvider` 등록은 `User` 상태 변경과 무관한 독립 쓰기 작업이다.
같은 AR에 묶으면 단순한 Device 작업에서도 `User` 전체를 포함한 집합 단위의 락이 발생한다.

### 3. 생명주기 cascade는 AR 경계 기준이 아니다

DDD에서 AR 경계의 기준은 **"항상 함께 변해야 하는 불변식(invariant)이 있는가"** 이다.
`User` 삭제 시 `Device`/`AuthProvider`가 함께 삭제되는 것은 사실이지만,
이는 `UserWithdrawalOrchestrator` + DB FK 제약으로 처리되는 **애플리케이션 레벨 cascade 정책**이다.

`User`↔`Device`, `User`↔`AuthProvider` 사이에 동시 일관성을 보장해야 하는 도메인 불변식이 없으므로,
같은 AR 경계 안에 묶을 이유가 없다.

### 4. Vaughn Vernon의 실용적 AR 원칙에 부합한다

Eric Evans의 원칙("하위 엔티티는 AR을 통해서만 접근")은 동시 변경 시 불변식 보호가 목적이다.
Vaughn Vernon(*Implementing Domain-Driven Design*)은 이를 실용적으로 완화하여
**"consistency boundary 기준으로 AR을 작게 유지하라"** 고 권고한다.

`Device`와 `UserAuthProvider`는 `User`와 동시 일관성 보장이 필요한 불변식이 없으므로
독립 AR로 취급하는 것이 더 적합하다.

---

## 결과

- `Device`, `UserAuthProvider`는 각각 독립 패키지(`device/`, `authprovider/`)와 독립 Repository를 가진다.
- `User` 삭제 시 연쇄 삭제는 `UserWithdrawalOrchestrator`가 각 Repository를 순서대로 호출하는 방식으로 처리한다.
- 향후 `Device`나 `UserAuthProvider`에 `User`와의 동시 일관성이 필요한 불변식이 생기면 이 결정을 재검토한다.

---

## 고려했으나 채택하지 않은 대안

### `account/device/`, `account/authprovider/` 하위 패키지로 이동

`User` 하위임을 패키지 구조로 명시적으로 표현하는 방안.
그러나 패키지 구조가 AR 경계를 표현해야 한다면, 독립 Repository를 가진 상태에서
하위 패키지로 이동하는 것은 오히려 구조적 모순을 야기한다.
접근 패턴과 트랜잭션 경계가 독립적임을 감안하면 현행 독립 패키지 구조가 더 정직하다.