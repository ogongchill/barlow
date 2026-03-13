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
