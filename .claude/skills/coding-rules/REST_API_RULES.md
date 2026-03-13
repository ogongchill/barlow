# Barlow — RESTful API 설계 가이드라인

> 이 문서는 Barlow 프로젝트에서 새 API 엔드포인트를 설계하거나 기존 API를 수정할 때 따라야 하는 규칙을 정의한다.
> 모든 규칙은 실제 코드베이스에서 발생한 위반 사례를 바탕으로 도출되었다.

---

## 1. URL 설계 원칙

### 1-1. 리소스는 명사, 복수형

| 나쁜 예 | 좋은 예 |
|---|---|
| `/api/v1/account` | `/api/v1/accounts` |
| `/api/v1/term` | `/api/v1/terms` |
| `/api/v1/recent-bill` | `/api/v1/recent-bills` |
| `/api/v1/client-version` | `/api/v1/client-versions` |

단, 단수형이 자연스러운 싱글톤 리소스(사용자의 프로필 등)는 예외로 허용한다.

```
GET /api/v1/accounts/me          ✅  (자신의 계정 — 싱글톤)
GET /api/v1/accounts/{id}        ✅  (특정 계정)
```

### 1-2. 행위를 URL에 포함하지 않는다

URL은 리소스를 식별하고, 행위는 HTTP 메서드로 표현한다.

| 나쁜 예 | 좋은 예 | HTTP 메서드 |
|---|---|---|
| `POST /subscriptions/activate` | `POST /subscriptions` | POST (생성) |
| `POST /subscriptions/deactivate` | `DELETE /subscriptions` | DELETE (삭제) |
| `POST /notifications/on` | `PATCH /notifications/{id}` | PATCH (부분 변경) |
| `GET /terms/active` | `GET /terms` | GET (필터는 쿼리 파라미터) |
| `GET /recent-bills/thumbnail` | `GET /recent-bills` | GET |
| `GET /client-versions/check` | `GET /client-versions` | GET |
| `POST /accounts/withdraw` | `DELETE /accounts/me` | DELETE |
| `GET /accounts/my` | `GET /accounts/me` | GET |

**예외**: 순수하게 행위만 존재하는 RPC성 작업 (예: 역할 전환)은 `/role`, `/promote` 같은 서브리소스를 허용한다.

### 1-3. 계층 구조는 포함 관계만 표현한다

부모-자식 관계만 URL 계층으로 표현한다. 조회 조건·필터는 쿼리 파라미터를 사용한다.

```
# 포함 관계 ✅
GET /api/v1/legislation-accounts/{legislationType}/subscriptions
GET /api/v1/legislation-accounts/{legislationType}/notification-settings
GET /api/v1/legislation-accounts/bill-posts/{billId}   ✅ (billId가 전역 고유 식별자이므로 부모 생략 허용)

# 필터를 URL 계층으로 잘못 표현 ❌
GET /api/v1/bill-posts/recent/thumbnail
# 올바른 표현 ✅
GET /api/v1/bill-posts?type=recent
```

### 1-4. 케밥 케이스 (kebab-case)

URL 경로 세그먼트는 소문자 케밥 케이스를 사용한다.

```
/api/v1/legislation-accounts   ✅
/api/v1/legislationAccounts    ❌
/api/v1/legislation_accounts   ❌
```

### 1-5. Path Variable vs Query Parameter 선택 기준

| 구분 | 사용 시점 | 예시 |
|---|---|---|
| `@PathVariable` | 리소스를 **식별**하는 값 | `/{billId}`, `/{legislationType}` |
| `@RequestParam` | **필터링·정렬·페이징** 조건 | `?page=0&size=20&sort=desc` |
| `@RequestBody` | **상태 변경**에 필요한 데이터 | `{"active": true}`, `{"reactionType": "LIKE"}` |

```java
// 식별자 → PathVariable ✅
@GetMapping("/{billId}")
public ApiResponse<BillPostDetailResponse> getDetail(@PathVariable Long billId) { ... }

// 필터 → RequestParam ✅
@GetMapping
public ApiResponse<List<BillPostResponse>> list(@RequestParam(required = false) String keyword) { ... }

// 상태 변경 데이터 → RequestBody ✅
@PostMapping("/{targetId}")
@ResponseStatus(HttpStatus.CREATED)
public ApiResponse<Void> react(@PathVariable String targetId, @RequestBody ReactionRequest request) { ... }

// 삭제 시 필터 조건 → RequestParam ✅ (Body 사용 금지)
@DeleteMapping("/{targetId}")
public ApiResponse<Void> removeReaction(
    @PathVariable String targetId,
    @RequestParam("targetType") String targetType,
    @RequestParam("reactionType") String reactionType) { ... }
```

---

## 2. HTTP 메서드 선택 기준

| 메서드 | 사용 시점 | 멱등성 | 바디 |
|---|---|---|---|
| `GET` | 리소스 조회 | O | X |
| `POST` | 리소스 생성, 복잡한 조회 | X | O |
| `PUT` | 리소스 전체 교체 | O | O |
| `PATCH` | 리소스 부분 변경 | △ | O |
| `DELETE` | 리소스 삭제 | O | X (필터는 쿼리 파라미터) |

### 메서드별 판단 흐름

```
새 리소스를 만드는가?       → POST  (201 Created)
기존 리소스를 삭제하는가?   → DELETE (200 OK)
특정 필드만 수정하는가?     → PATCH (200 OK)
리소스 전체를 교체하는가?   → PUT   (200 OK)
조회인가?                   → GET   (200 OK)
```

### Toggle 패턴 — PATCH + boolean body

on/off처럼 상태를 전환할 때 `POST /activate` + `POST /deactivate` 두 엔드포인트 대신 하나의 `PATCH`로 통합한다.

```java
// 잘못된 패턴 ❌
@PostMapping("/notification-setting/on")
public ApiResponse<Void> on(...) { ... }

@PostMapping("/notification-setting/off")
public ApiResponse<Void> off(...) { ... }

// 올바른 패턴 ✅
@PatchMapping
public ApiResponse<Void> updateNotificationSetting(
    @PathVariable LegislationType legislationType,
    @PassportUser Passport passport,
    @RequestBody NotificationSettingRequest request) {
    if (request.active()) { service.activate(...); }
    else { service.deactivate(...); }
    return ApiResponse.success();
}
```

---

## 3. HTTP 상태코드 사용 기준

### 성공 응답

| 코드 | 사용 시점 | Spring 어노테이션 |
|---|---|---|
| `200 OK` | 일반 성공 (조회, 수정, 삭제) | 기본값 (생략 가능) |
| `201 Created` | 리소스 생성 성공 | `@ResponseStatus(HttpStatus.CREATED)` |
| `204 No Content` | 응답 바디 없는 성공 | `@ResponseStatus(HttpStatus.NO_CONTENT)` |

**Barlow 프로젝트 관례**: 삭제 성공도 `ApiResponse<Void>`를 반환하므로 `200 OK`를 사용한다.

```java
// 생성 → 201 ✅
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public ApiResponse<Void> subscribe(...) { return ApiResponse.success(); }

// 삭제 → 200 (Barlow 관례) ✅
@DeleteMapping
public ApiResponse<Void> unsubscribe(...) { return ApiResponse.success(); }
```

### 오류 응답 (CoreApiErrorType)

`ERROR_HANDLING.md` 참조

---

## 4. 요청·응답 구조

### 4-1. 응답 봉투 (ApiResponse)

`ERROR_HANDLING.md` 참조

### 4-2. 요청 DTO — record 사용

요청 DTO는 `record`로 선언한다. 검증이 필요한 경우 `Validatable` 인터페이스를 구현한다.

```java
// 기본 요청 record ✅
public record ReactionRequest(ReactionTarget targetType, ReactionType reactionType) {}

// 검증 포함 ✅
public record CreateBillPostRequest(String title, String content) implements Validatable {
    @Override
    public void validate() {
        if (!StringUtils.hasText(title)) {
            throw new CoreApiException(CoreApiErrorType.BAD_REQUEST, "title은 필수입니다.");
        }
    }
}
```

컨트롤러에서 검증 실행:

```java
@PostMapping
public ApiResponse<Void> create(@RequestBody CreateBillPostRequest request) {
    request.validate();   // Validatable 구현 시
    service.create(request);
    return ApiResponse.success();
}
```

### 4-3. 페이징 요청

페이징 파라미터는 `PagingSortFilterRequest`를 상속해 일관성을 유지한다.

```java
// record는 클래스를 상속할 수 없으므로 class로 선언한다
public class BillPostSearchRequest extends PagingSortFilterRequest {
    private String keyword;
    // PagingSortFilterRequest가 page, size 등 공통 파라미터를 정의
}
```

---

## 5. 인증 패턴

### 5-1. 인증 필요 엔드포인트

`@PassportUser Passport passport` 파라미터를 선언하면 `InboundJwtAuthenticationFilter`가 검증한 사용자 정보가 주입된다.

```java
@GetMapping("/me")
public ApiResponse<AccountResponse> getMyAccount(@PassportUser Passport passport) {
    return ApiResponse.success(accountService.getAccount(passport.getUser()));
}
```

### 5-2. 공개 엔드포인트 — shouldNotFilter 화이트리스트

인증 없이 접근 가능한 엔드포인트는 `InboundJwtAuthenticationFilter.shouldNotFilter()`에 반드시 등록해야 한다.

```java
// infra/auth/.../filter/InboundJwtAuthenticationFilter.java
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    return match("/api/v1/auth/guests", request)
        || match("/api/v1/auth/guest/sessions", request)
        || match("/api/v1/auth/oidc/accounts", request)
        || match("/api/v1/auth/oidc/sessions", request)
        || match("/api/v1/terms/**", request)
        || match("/health", request)
        || match("/actuator/**", request)
        || match("/error", request);
}
```

**공개 엔드포인트 URL 변경 시 반드시 이 파일도 함께 수정한다.**

---

## 6. 컨트롤러 레이어 구현 규칙

### 6-1. 컨트롤러 책임 범위

컨트롤러는 아래만 담당한다. 비즈니스 로직은 `core:service`로 위임한다.

1. 요청 파라미터 수신
2. `request.validate()` 호출 (Validatable 구현 시)
3. `service.method()` 위임
4. `ApiResponse` 반환

```java
// 올바른 컨트롤러 ✅
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public ApiResponse<Void> subscribe(
    @PathVariable LegislationType legislationType,
    @PassportUser Passport passport) {
    legislationAccountSubscribeService.subscribeAccount(legislationType, passport.getUser());
    return ApiResponse.success();
}
```

### 6-2. infra 직접 참조 금지

컨트롤러에서 `infra:*` 패키지를 직접 import해서는 안 된다.

```java
// ❌ 금지
import com.barlow.infra.storage.jpa.account.AccountJpaRepository;

// ✅ core:service 인터페이스만 주입
import com.barlow.core.service.account.business.AccountService;
```

### 6-3. 로깅

의미 있는 요청 처리 시 `log.info()`로 수신 사실을 기록한다. 민감 정보(토큰, 비밀번호)는 로그에 포함하지 않는다.

```java
private static final Logger log = LoggerFactory.getLogger(LegislationAccountSubscribeController.class);

log.info("Received {} account subscribe request.", legislationType);
```

---

## 7. REST Docs 테스트 작성 규칙

`TESTING.md §5` 참조.

---

## 8. 체크리스트

새 API 엔드포인트를 추가하거나 수정할 때 아래 항목을 확인한다.

- [ ] URL에 동사(activate, deactivate, check, get, create 등)가 포함되어 있지 않은가?
- [ ] 컬렉션 리소스 URL이 복수형인가? (`/terms`, `/accounts`, `/recent-bills`)
- [ ] HTTP 메서드가 의미에 맞게 선택되었는가? (생성→POST, 삭제→DELETE, 부분수정→PATCH)
- [ ] 생성 엔드포인트에 `@ResponseStatus(HttpStatus.CREATED)`가 붙어 있는가?
- [ ] POST/PATCH 요청 데이터를 `@RequestBody`로 수신하는가?
- [ ] 공개 엔드포인트라면 `InboundJwtAuthenticationFilter.shouldNotFilter()`에 등록했는가?
- [ ] 컨트롤러에서 `infra:*` 패키지를 직접 import하지 않는가?
- [ ] REST Docs 테스트가 실제 HTTP 메서드(`.post()`, `.delete()`, `.patch()`)와 일치하는가?
