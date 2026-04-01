# core:service 모듈

비즈니스 로직을 담는 서비스 계층 모듈. `core:domain`의 Repository 인터페이스를 사용하며 `infra:*`를 직접 참조하지 않습니다.

**의존 방향**: `app:api → core:service → core:domain ← infra:*`

---

## 레이어 구조

각 BC는 두 레이어로 나뉩니다.

| 레이어 | 어노테이션 | 역할 |
|--------|-----------|------|
| `business` | `@Service` | 트랜잭션 경계, 공개 API, 도메인 로직 조율 |
| `impl` | `@Component` | Repository 래핑, 세부 구현, 상태 변경 |

`app:api`는 `business` 레이어만 참조합니다. `impl` 레이어는 `business`에서만 주입받아 사용합니다.

---

## BC 구성

### Account

사용자 계정 생성·로그인·탈퇴를 담당합니다.

**Business**
- `AccountCreateService` — Guest/Member 신규 생성 (약관 검증, 알림 초기화)
- `AccountLoginService` — 로그인 처리 (디바이스 갱신)
- `AccountWithdrawalService` — 회원 탈퇴 진입점
- `MemberRegisterService` — Guest → Member 전환 및 신규 Member 가입
- `MyAccountRetrieveService` — 내 계정 정보 조회

**Impl**
- `UserCreator` / `UserReader` — 사용자 생성·조회
- `DeviceReader` / `DeviceRefresher` — 기기 조회·토큰 갱신
- `TermManager` — 약관 동의 처리
- `UserWithdrawalOrchestrator` — Role별 탈퇴 전략 디스패치 (전략 패턴)
- `GuestUserWithdrawalProcessor` / `MemberUserWithdrawalProcessor` — 탈퇴 구현체

### Home

홈 화면과 알림 센터 조회를 담당합니다.

**Business**
- `HomeRetrieveFacade` — 홈 화면 데이터 통합 (Facade 패턴)
- `MyHomeInfoRetrieveService` — 구독 중인 입법기관 + 알림 도착 여부
- `TodayBillPostThumbnailRetrieveService` — 오늘의 법안 썸네일
- `NotificationCenterItemRetrieveService` — 알림 센터 항목

**Impl**
- `MyHomeInfoReader` — Repository 조회 조합
- `NotificationCenterItemReader` — 알림 항목 조회

### LegislationAccount

입법기관 계정 조회와 구독 관리를 담당합니다.

**Business**
- `LegislationAccountRetrieveService` — 입법기관 프로필·법안·알림·구독 정보 조회
- `LegislationAccountSubscribeService` — 구독·구독 해제

**Impl**
- `LegislationAccountReader` — 알림 설정·구독 정보 조합 조회
- `LegislationAccountSubscriptionManager` — 구독 활성화 + 카운터 동기화
- `LegislationAccountWithdrawalHandler` — 탈퇴 시 구독 정리

### BillPost

법안 게시물 조회와 조회수 추적을 담당합니다.

**Business**
- `BillPostRetrieveService` — 법안 목록·상세 조회 + 조회수 추적

**Impl**
- `BillPostReader` — 법안 검색
- `BillPostCacheService` — Caffeine 캐시 적용 (상세 30분, 조회수 중복 제거 1시간)
- `BillPostViewCountUpdater` — 조회수 업데이트

### NotificationSetting

사용자 알림 설정 관리를 담당합니다.

**Business**
- `NotificationSettingService` — 알림 설정 활성화·비활성화

**Impl**
- `NotificationSettingActivator` — 상태 변경 + 기본값 초기화
- `NotificationSettingReader` — 알림 설정 조회
- `NotificationWithdrawalHandler` — 탈퇴 시 알림 설정·센터 정리

### Reaction

반응(좋아요 등) 조회·생성·삭제를 담당합니다.

**Business**
- `ReactionService` — 반응 조회·추가·삭제

**Impl**
- `ReactionReader` — 반응 상태 조회
- `ReactionProcessor` — 반응 상태 검증 및 처리

### Subscribe

구독 상태 관리를 담당합니다. Business 레이어 없이 impl만 존재하며 다른 BC에서 주입받아 사용합니다.

- `SubscriptionActivator` — 구독 활성화·비활성화
- `SubscriptionReader` — 구독 정보 조회
- `SubscriptionWithdrawalHandler` — 탈퇴 시 구독 삭제

### Menu

알림 설정 메뉴 조회와 토글을 담당합니다.

**Business**
- `MenuFacade` — 알림 설정·메뉴 데이터 통합 (Facade 패턴)
- `MenuService` — 알림 설정 메뉴 데이터 제공

### Version

클라이언트 버전 검증을 담당합니다.

**Business**
- `ClientVersionService` — `ClientVersionPolicy`를 사용해 버전 상태 반환

---

## 주요 패턴

### Facade

여러 Service를 조합해 단일 진입점을 제공합니다.

```
HomeRetrieveFacade
  → MyHomeInfoRetrieveService
  → TodayBillPostThumbnailRetrieveService
  → NotificationCenterItemRetrieveService

MenuFacade
  → NotificationSettingService
  → MenuService
```

### 전략 패턴 (UserWithdrawalOrchestrator)

`UserWithdrawalProcessor` 구현체를 Role로 매핑해 탈퇴 흐름을 분기합니다.

```
UserWithdrawalOrchestrator
  → GUEST  → GuestUserWithdrawalProcessor  (User + Device 삭제)
  → MEMBER → MemberUserWithdrawalProcessor (User + Device + Auth + 부수 정리)
               → NotificationWithdrawalHandler
               → SubscriptionWithdrawalHandler
               → LegislationAccountWithdrawalHandler
```

### 캐싱 (BillPostCacheService)

Caffeine 로컬 캐시를 데코레이터 형태로 적용합니다.

- 법안 상세: `maximumSize=500`, `expireAfterWrite=30m`
- 조회수 중복 제거: `maximumSize=10_000`, `expireAfterWrite=1h`

---

## 의존 모듈

```gradle
implementation project(":core:domain")
implementation 'com.github.ben-manes.caffeine:caffeine'
implementation 'org.springframework:spring-context-support'
compileOnly 'org.springframework:spring-context'
compileOnly 'org.springframework:spring-tx'
```
