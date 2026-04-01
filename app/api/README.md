# app:api 모듈

클라이언트 HTTP 요청을 처리하고 `core:service` 계층과 협력하여 응답을 반환하는 진입점 모듈.  
**의존 방향**: `app:api → core:service → core:domain ← infra:*`

---

## 패키지 구조

```
com.barlow.app/
├── api/
│   ├── config/                    # 필터 · 인터셉터 · 비동기 설정
│   ├── controller/
│   │   ├── CoreApiControllerAdvice.java   # 전역 예외 처리
│   │   ├── HealthCheckController.java     # GET /health
│   │   └── v1/
│   │       ├── auth/              # 인증 (회원가입 · 로그인)
│   │       ├── account/           # 계정 관리
│   │       ├── home/              # 홈 화면 · 알림 센터
│   │       ├── menu/              # 알림 설정 메뉴
│   │       ├── reaction/          # 반응
│   │       ├── recent-bill/       # 최근 법안
│   │       ├── pre-announce/      # 사전공시 법안
│   │       ├── legislationaccount/ # 입법기관 계정
│   │       ├── term/              # 약관
│   │       └── version/           # 클라이언트 버전 체크
├── scheduler/                     # 배치 Lambda 호출 스케줄러
└── support/
    ├── error/                     # CoreApiException · CoreApiErrorType
    ├── response/                  # ApiResponse 래퍼
    └── validate/                  # Validatable (요청 DTO 검증 인터페이스)
```

---

## 인증 및 보안

### 필터 체인 (실행 순서)

```
요청
 ├─[order=1] HandlerValidationFilter          — 핸들러 없으면 404 즉시 반환
 ├─[order=2] FilterExceptionHandler           — 필터 레벨 예외 처리
 └─[order=3] InboundJwtAuthenticationFilter   — JWT 검증
```

### 인터셉터

- **GuestPassportAuthorizationInterceptor** — 인증된 `Passport` 객체 바인딩
- **TraceLoggingInterceptor** — 요청 추적 로그

### @PassportUser

컨트롤러 메서드 파라미터에 선언하면 `PassportUserArgumentResolver`가 인증된 `Passport` 객체를 주입.  
`Passport`는 Guest / Member 역할을 추상화합니다.

---

## 응답 포맷

모든 엔드포인트는 `ApiResponse<T>` 래퍼로 응답합니다.

```
{ "result": "SUCCESS" | "ERROR", "data": ..., "error": { "code": "E4xx|E500", "message": "..." } }
```

서비스 레이어 도메인 DTO → API 응답 DTO 변환은 **ApiSpecComposer** 클래스가 담당합니다.

---

## 에러 처리

`CoreApiControllerAdvice`가 예외를 레벨별로 분류해 로그를 남기고, IMPLEMENTATION 레벨 이상은 Alert를 발송합니다.

| 코드 | HTTP | 의미 |
|------|------|------|
| E400 | 400 | 잘못된 요청 |
| E401 | 401 | 인증 필요 |
| E403 | 403 | 권한 없음 |
| E404 | 404 | 리소스 없음 |
| E409 | 409 | 충돌 |
| E500 | 500 | 서버 오류 |

---

## 설정

`application.yml`은 `auth.yml`, `storage-core.yml`, `logging.yml`, `monitoring.yml`, `alert.yml`을 import합니다.

프로파일: `local` · `dev` · `staging` · `live`

---

## 스케줄러

`BatchLambdaScheduler`가 배치 Lambda 함수를 HTTP로 호출합니다 (`staging` 프로파일에서 비활성화).

- 사전공시 법안 추적: 평일 12:00
- 법안 정보 추적: 평일 15:30
- 오늘의 법안 업데이트: 평일 19:00

---

## 의존 모듈

```gradle
implementation project(":core:domain")
implementation project(":core:service")
implementation project(":infra:auth")
implementation project(":infra:post-view")
implementation project(":support:logging")
implementation project(":support:monitoring")
implementation project(":support:alert")
runtimeOnly  project(":infra:storage")
```
