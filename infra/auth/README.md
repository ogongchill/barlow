# infra:auth 모듈

JWT 기반 액세스 토큰 발급·검증과 OIDC 소셜 로그인을 처리하는 인증 인프라 모듈.  
필터·인터셉터·ArgumentResolver를 포함하며 `app:api`에서 주입받아 사용합니다.

---

## 패키지 구조

```
com.barlow.infra.auth/
├── authentication/
│   ├── core/          # 인증 추상화 (Authenticator, Credential, Principal)
│   ├── token/         # JWT 액세스 토큰 발급·검증
│   └── oauth/         # OIDC 소셜 로그인 (Kakao)
├── config/            # RSA 키, JWT 설정 Bean
└── support/
    ├── annotation/    # @PassportUser
    ├── error/         # CoreAuthException
    ├── filter/        # InboundJwtAuthenticationFilter, FilterExceptionHandler
    ├── interceptor/   # Passport 권한 검증, 요청 추적
    └── resolver/      # PassportUserArgumentResolver
```

---

## 인증 흐름

```
요청
 ├─ FilterExceptionHandler           — 필터 레벨 예외 처리
 └─ InboundJwtAuthenticationFilter   — Bearer 토큰 추출 → JWT 검증 → Passport 생성
      ↓
 GuestPassportAuthorizationInterceptor  — Passport 존재 확인 + 최소 Guest 이상 검증
 TraceLoggingInterceptor               — 요청 추적 로그 (ThreadContext)
      ↓
 PassportUserArgumentResolver          — @PassportUser 파라미터에 Passport 주입
```

### 필터 제외 경로

인증 없이 접근 가능한 경로는 `InboundJwtAuthenticationFilter`의 `shouldNotFilter()`로 관리합니다.

- `/api/v1/auth/guests`, `/api/v1/auth/guest/sessions`
- `/api/v1/auth/oidc/accounts`, `/api/v1/auth/oidc/sessions`
- `/api/v1/terms/**`
- `/health`, `/actuator/**`, `/error`

---

## JWT 액세스 토큰

- **알고리즘**: RSA256 (개인키로 서명, 공개키로 검증)
- **발급자(iss)**: `barlow-core-auth`
- **Claims**: `memberNo`, `role`
- **유효 기간**: AccessToken 1일 / RefreshToken 30일

RSA 키는 환경변수(`JWT_CRYPTO_PRIVATE_KEY`, `JWT_CRYPTO_PUBLIC_KEY`)로 주입합니다.

---

## OIDC 소셜 로그인

`OidcAuthenticationService`가 Provider별 `OidcAuthenticator` 구현체로 라우팅합니다.

| Provider | 상태 | 검증 방식 |
|----------|------|---------|
| Kakao | 구현 완료 | JWKS 엔드포인트에서 공개키 조회 후 RSA256 검증 |
| Naver | 미구현 | — |

새로운 Provider 추가 시 `OauthProvider` 열거형 확장 → `IdToken` 서브클래스 생성 → `OidcAuthenticator` 구현 → `OidcAuthenticationService` 라우팅 추가 순으로 진행합니다.

---

## Passport

인증된 사용자의 요청 컨텍스트 객체. `InboundJwtAuthenticationFilter`에서 생성되어 요청 속성에 저장됩니다.

- `core:domain`에 정의된 불변 객체
- `User`(userNo, role) + deviceId + clientOs 정보를 포함
- 컨트롤러 메서드에서 `@PassportUser Passport passport`로 주입받아 사용

---

## 설정 (auth.yml)

```yaml
auth:
  jwt:
    crypto:
      private-key: ${JWT_CRYPTO_PRIVATE_KEY}
      public-key: ${JWT_CRYPTO_PUBLIC_KEY}
  oidc:
    kakao:
      jwk-uri: ${KAKAO_OIDC_JWK_URL}
      iss: ${KAKAO_OIDC_ISS}
      aud: ${KAKAO_OIDC_AUD}
```

---

## 의존 모듈

```gradle
compileOnly 'org.springframework.boot:spring-boot-starter-web'
compileOnly project(":core:domain")
implementation 'com.auth0:java-jwt:4.4.0'
implementation 'com.auth0:jwks-rsa:0.22.1'
```
