# Error Handling Rules

## 1. 예외 계층 구조

예외는 발생하는 레이어에 따라 3종으로 명확히 구분한다.

```
[Domain Layer]   CoreDomainException   — 순수 Java, HTTP/Spring 의존 없음
[Auth Layer]     CoreAuthException     — 인증 서비스(OIDC, JWT) 전용
[API Layer]      CoreApiException      — Controller / Service / Filter
```

---

### CoreDomainException — 도메인 계층

**사용처:** 도메인 객체(AR, VO, Policy) 내부에서 비즈니스 규칙이 위반되었을 때.

- `@Entity`, `HttpStatus`, Spring Bean 등 HTTP·인프라 의존성 전혀 없음
- `CoreDomainException`을 직접 throw하지 않는다. **BC별 하위 예외 클래스 + static factory 패턴.**

```java
// GOOD — BC별 예외 클래스 + static factory
public class SubscriptionDomainException extends CoreDomainException {

    private SubscriptionDomainException(CoreDomainExceptionType type, String message) {
        super(type, message);
    }

    public static SubscriptionDomainException alreadySubscribed(LegislationType type) {
        return new SubscriptionDomainException(
            CoreDomainExceptionType.CONFLICT_EXCEPTION,
            "이미 구독중인 입법계정 " + type + " 입니다");
    }

    public static SubscriptionDomainException alreadyUnSubscribed(LegislationType type) {
        return new SubscriptionDomainException(
            CoreDomainExceptionType.CONFLICT_EXCEPTION,
            "이미 구독 취소한 입법계정 " + type + " 입니다");
    }
}

// GOOD — 프로그래밍 계약 위반(precondition check): Java 표준 예외 허용
public SemanticVersion(int major, int minor, int patch, VersionSuffix suffix) {
    if (major < 0) {
        throw new IllegalArgumentException("major는 0 이상이어야 합니다");  // OK
    }
}

// BAD — 비즈니스 규칙 위반에 표준 예외 사용 (500 catch-all로 빠짐)
throw new IllegalStateException("이미 구독 취소 상태");

// BAD — API 레이어 예외를 도메인에서 사용
throw new CoreApiException(CoreApiErrorType.CONFLICT, "...");
```

---

### CoreApiException — API 계층

**사용처:** Controller, Service, Filter/Interceptor 등 애플리케이션 레이어.

```java
// GOOD
throw new CoreApiException(CoreApiErrorType.NOT_FOUND);
throw CoreApiException.badRequest("deviceId는 필수입니다.");

// BAD — 도메인 예외를 API 레이어에서 throw
throw new AccountDomainException(CoreDomainExceptionType.NOT_FOUND_EXCEPTION, "...");
```

---

### CoreAuthException — 인증 서비스 전용

`infra:auth` 모듈 내부에서만 사용한다. 다른 모듈에서 직접 throw하지 않는다.

---

## 2. 응답 포맷

모든 API 응답은 `ApiResponse<T>`로 감싸인다.

**성공:**
```json
{ "result": "SUCCESS", "data": { ... }, "error": null }
```

**실패:**
```json
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "E404",
    "message": "The requested resource could not be found.",
    "data": null
  }
}
```

`error.message`는 클라이언트에게 노출되는 **범용 고정 메시지**다. 구현 세부 정보를 담아서는 안 된다.
**클라이언트는 `error.code`를 기반으로 프론트엔드 로직을 분기한다.**

---

## 3. 메시지 전략 — Hybrid

| 대상 | 내용 | 전달 위치 |
|---|---|---|
| 클라이언트 | Enum 고정 메시지 (범용) | JSON 응답 `error.message` |
| 서버 | 호출부 동적 상세 메시지 | 서버 로그만 |

```java
// CoreDomainException — message는 서버 로그에만 기록
throw AccountDomainException.notFound();
// → 로그: "계정이 존재하지 않습니다"
// → 클라이언트: "The requested resource could not be found." (고정)

// CoreApiException — data를 클라이언트용 컨텍스트로 활용 시
throw new CoreApiException(CoreApiErrorType.BAD_REQUEST, Map.of("nickname", "공백 불가"));
// → 클라이언트: error.data에 포함 (클라이언트가 처리해야 할 맥락)
```

### `error.data` 사용 기준

```java
// GOOD — 클라이언트가 스스로 처리하기 위해 필요한 맥락
throw new CoreApiException(CoreApiErrorType.BAD_REQUEST, Map.of("nickname", "공백 불가", "email", "형식 오류"));

// BAD — 내부 구현 세부사항
throw new CoreApiException(CoreApiErrorType.NOT_FOUND, "SELECT * FROM users WHERE id=" + userId);
```

---

## 4. 로깅 & 알럿 전략

### CoreDomainExceptionLevel

| Level | 의미 | 로그 레벨 | Alert |
|---|---|---|---|
| `BUSINESS` | 사용자 행위에 의한 정상 거절 (중복 구독, 이미 취소 등) | WARN | 없음 |
| `IMPLEMENTATION` | 발생해서는 안 되는 버그 또는 데이터 불일치 | ERROR | 즉각 알럿 발송 |

```java
// BUSINESS — 정상적 비즈니스 거절 → WARN
public static SubscriptionDomainException alreadySubscribed(LegislationType type) {
    return new SubscriptionDomainException(
        CoreDomainExceptionCode.E409, CoreDomainExceptionLevel.BUSINESS,
        "이미 구독중인 입법계정: " + type);
}

// IMPLEMENTATION — 있어야 할 데이터가 없음 (버그 가능성) → ERROR + Alert
public static BillPostDomainException notFound(String billId) {
    return new BillPostDomainException(
        CoreDomainExceptionCode.E500, CoreDomainExceptionLevel.IMPLEMENTATION,
        "법안을 찾을 수 없습니다: billId=" + billId);
}
```

### CoreApiErrorType LogLevel

| LogLevel | 상황 | Alert |
|---|---|---|
| `WARN` | 예상 가능한 클라이언트 에러 (400, 401, 403, 404, 409) | 없음 |
| `ERROR` | 예상치 못한 서버 에러 (500) | 즉각 알럿 발송 |

---

## 5. ControllerAdvice 처리 흐름

```
요청
 ↓
CoreDomainException  → level 분기
                          BUSINESS      → WARN 로그
                          IMPLEMENTATION → ERROR 로그 + Alert
                       findByErrorCode() → HTTP 상태 결정

CoreAuthException    → logLevel 분기 (동일)

CoreApiException     → logLevel 분기 (동일)

Exception (catch-all) → ERROR 로그 + Alert + 500 반환
```

**모든 예외는 반드시 위 3개 타입 중 하나로 감싸서 throw한다.**

---

## 6. 새 예외 추가 가이드

### Case A: 도메인 계층 — 새 BC 예외 추가

1. 해당 도메인 패키지에 `CoreDomainException` 상속 클래스 생성. static factory 패턴 적용. (§1 예시 참조)
2. 도메인 객체 내부에서 throw.
3. ControllerAdvice 변경 불필요 — `CoreDomainException` 핸들러가 자동 처리.

### Case B: API 레이어 — 새 에러 타입 추가

```java
// 1단계 — CoreApiErrorCode에 새 코드 추가
// 2단계 — CoreApiErrorType에 새 타입 추가
UNPROCESSABLE(HttpStatus.UNPROCESSABLE_ENTITY, E422, "Request could not be processed.", WARN),
// 3단계 — CoreApiException에 static factory 추가 (선택)
// 4단계 — ControllerAdvice 변경 불필요.
```

---

## 7. 위반 사례

| 위반 패턴 | 올바른 방향 |
|---|---|
| `IMPLEMENTATION` 레벨인데 WARN 로그 | ERROR 로그 + Alert 필수 |
| `Exception` 직접 throw | `CoreApiException` 또는 `CoreDomainException` 하위로 감싸서 throw |
| 새 예외 클래스가 `Exception` / `RuntimeException` 직접 상속 | 반드시 `CoreDomainException` 또는 `CoreApiException` 상속 |
| `CoreDomainException` 직접 throw | BC별 하위 예외 클래스 생성 후 사용 |