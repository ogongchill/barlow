# Barlow 도메인 백과사전 (Ubiquitous Language Encyclopedia)

> 이 문서는 한국어 도메인 용어 ↔ 코드 클래스/패키지 간의 완전한 매핑을 정의한다.
> "법안이라고 말하면 BillPost를 찾아야 한다"는 식의 문제를 해결하기 위한 단일 진실의 소스.
>
> **갱신 원칙**: 새 BC/클래스 추가 또는 리네임 시 반드시 이 문서를 함께 업데이트한다.

---

## 목차

1. [빠른 참조: 한국어 → 코드](#빠른-참조-한국어--코드)
2. [BC별 상세 정의](#bc별-상세-정의)
   - [Account BC (계정)](#1-account-bc-계정)
   - [ExternalAuth BC (외부 인증)](#2-externalauth-bc-외부-인증)
   - [Device BC (디바이스)](#3-device-bc-디바이스)
   - [BillPost BC (법안)](#4-billpost-bc-법안)
   - [LegislationAccount BC (입법계정)](#5-legislationaccount-bc-입법계정)
   - [Subscribe BC (구독)](#6-subscribe-bc-구독)
   - [NotificationSetting BC (알림 설정)](#7-notificationsetting-bc-알림-설정)
   - [NotificationCenter BC (알림함)](#8-notificationcenter-bc-알림함)
   - [Reaction BC (리액션)](#9-reaction-bc-리액션)
   - [Version BC (버전)](#10-version-bc-버전)
3. [Cross-cutting 개념](#cross-cutting-개념)
4. [전체 Enum 정의](#전체-enum-정의)
5. [레이어별 네이밍 규칙](#레이어별-네이밍-규칙)
6. [BC 간 의존 관계](#bc-간-의존-관계)

---

## 빠른 참조: 한국어 → 코드

| 한국어 용어 | 영어 식별자 | 위치 (패키지) | 비고 |
|---|---|---|---|
| 사용자, 유저 | `User` | `core.domain` (루트) | userNo, Role(GUEST/MEMBER/ADMIN) |
| 여권, 인증 컨텍스트 | `Passport` | `core.domain` (루트) | User + Device 합성 |
| 계정, 회원 | Account BC | `core.domain.account` | User가 AR |
| 계정 프로필 | `AccountProfile` | `core.domain.account` | record |
| 내 계정 정보 | `MyAccountInfo` | `core.domain.account` | |
| 약관 | `Term` | `core.domain.account.term` | SERVICE/PRIVACY/MARKETING |
| 약관 동의 | `TermAgreement` | `core.domain.account.term` | record |
| 약관 정책 | `TermsPolicy` | `core.domain.account.term` | 필수 약관 검증 로직 |
| 외부 인증, 소셜 로그인 | `UserExternalAuth` | `core.domain.externalauth` | 카카오/네이버 |
| 외부 인증 주체 | `ExternalPrincipal` | `core.domain.externalauth` | authProvider + sub |
| 인증 제공자, 소셜 | `AuthProvider` | `core.enumerate` | KAKAO, NAVER |
| 디바이스, 기기 | `Device` | `core.domain.device` | deviceId, token, OS |
| 법안, 법안 게시물 | `BillPost` | `core.domain.billpost` | 핵심 AR |
| 법안 목록 | `BillPostsStatus` | `core.domain.billpost` | 페이지 포함 |
| 법안 필터 | `BillPostFilterTag` | `core.domain.billpost` | VO |
| 법안 발의자 | `BillProposer` | `core.domain.billpost` | record |
| 오늘의 법안 썸네일 | `TodayBillPostThumbnail` | `core.domain.billpost` | record |
| 소관위, 상임위, 위원회 | `LegislationType` | `core.enumerate` | 21개 enum값 |
| 입법 진행 상태 | `ProgressStatus` | `core.enumerate` | 접수~공포 12단계 |
| 발의 유형 | `ProposerType` | `core.enumerate` | 정부/위원장/의장/의원/기타 |
| 정당 | `PartyName` | `core.enumerate` | 9개 정당 |
| 입법계정, 위원회계정 | `LegislationAccount` | `core.domain.legislationaccount` | 위원회/정부 "계정" |
| 내 입법계정 | `MyLegislationAccount` | `core.domain.legislationaccount` | record |
| 구독 | `Subscription` | `core.domain.subscribe` | 패키지명은 `subscribe` 주의 |
| 알림 설정 | `NotificationSetting` | `core.domain.notificationsetting` | 토픽별 on/off |
| 알림 주제, 알림 토픽 | `NotificationTopic` | `core.enumerate` | 25개 enum값 |
| 알림함, 알림 센터 | `NotificationCenterItem` | `core.domain.notificationcenter` | 수신된 알림 |
| 리액션, 반응 | `Reaction` | `core.domain.reaction` | LIKE/DISLIKE/HMM |
| 리액션 대상 | `ReactionTarget` | `core.enumerate` | BILL_POST, COMMENT |
| 리액션 타입 | `ReactionType` | `core.enumerate` | LIKE, DISLIKE, HMM |
| 버전, 클라이언트 버전 | `ClientVersionPolicy` | `core.domain.version` | 최소/최신 버전 정책 |
| 시맨틱 버전 | `SemanticVersion` | `core.domain.version` | VO (major.minor.patch-suffix) |
| 버전 상태 | `ClientVersionStatus` | `core.enumerate` | LATEST/UPDATE_AVAILABLE/NEED_FORCE_UPDATE |
| 의원, 국회의원 | `LawmakerJpaEntity` | `infra.storage` | 영속성 계층 전용 (도메인 AR 없음) |

---

## BC별 상세 정의

### 1. Account BC (계정)

**도메인 용어**: 계정, 회원, 사용자, 게스트, 멤버, 회원가입, 탈퇴, 승격

**핵심 패키지**: `com.barlow.core.domain.account`

#### Aggregate Root
- **`User`** (`com.barlow.core.domain` — 루트 패키지에 위치)
  - 시스템의 모든 사용자를 나타냄
  - `userNo`: 사용자 식별자
  - `Role`: `GUEST` (비회원 앱 사용자) / `MEMBER` (가입 완료) / `ADMIN`
  - 핵심 규칙: GUEST만 MEMBER로 승격 가능 (`toGuestToMemberCommand()`)

#### Read Model
- **`AccountProfile`** — `record(userNo, nickname, role)` — 프로필 조회용
- **`MyAccountInfo`** — 내 계정 상세 정보

#### Commands (명령 객체)
| 클래스 | 한국어 의미 |
|---|---|
| `UserRegisterCommand` | 사용자 등록 (nickname, role) |
| `UserCreateCommand` | 사용자 생성 (내부용) |
| `MemberCreateCommand` | 멤버 생성 |
| `MemberLoginCommand` | 멤버 로그인 |
| `MemberPromoteCommand` | 게스트 → 멤버 승격 |
| `LoginCommand` | 로그인 |
| `GuestToMemberCommand` | 게스트→멤버 전환 (User 도메인 메서드에서 반환) |

#### Queries (쿼리 객체)
- **`UserQuery`** — 사용자 조회 조건

#### Exceptions
- **`AccountDomainException`** — 계정 도메인 예외 (modificationException / accountNotFound / notFound / forbidden)
- **`RegistrationException`** — 회원가입 예외 (requiredTermsNotAccepted / authProviderExists)

#### Repository
- **`UserRepository`** — retrieve / retrieveProfile / create / promoteToMember / findByProviderAndSub / delete

#### Sub-BC: Term (약관) — `account.term`
| 클래스 | 한국어 의미 |
|---|---|
| `Term` | 약관 본체 (id, title, version, linkUrl, Type, required, effectiveAt) |
| `Term.Type` | SERVICE(서비스) / PRIVACY(개인정보) / MARKETING(마케팅) |
| `TermAgreement` | 약관 동의 내역 (termId, agreed, agreedAt) |
| `TermsPolicy` | 약관 정책 — 필수 약관 미동의 시 RegistrationException 발생 |
| `UserTermAgreementCommand` | 약관 동의 명령 |
| `TermRepository` | 약관 저장소 인터페이스 |

#### Service 계층 (`core.service.account`)
- `AccountCreateService` — 계정 생성
- `AccountLoginService` — 로그인
- `AccountWithdrawalService` — 탈퇴
- `ExternalAuthService` — 외부 인증 연동
- `MemberRegisterService` — 멤버 회원가입
- `MyAccountRetrieveService` — 내 계정 조회
- `UserWithdrawalOrchestrator` — 탈퇴 오케스트레이터
- `GuestUserWithdrawalProcessor` / `MemberUserWithdrawalProcessor` — 역할별 탈퇴 처리

---

### 2. ExternalAuth BC (외부 인증)

**도메인 용어**: 소셜 로그인, 카카오 로그인, 네이버 로그인, OIDC, 외부 인증, 인증 제공자

**핵심 패키지**: `com.barlow.core.domain.externalauth`

#### Aggregate Root
- **`UserExternalAuth`** — 특정 사용자의 외부 인증 정보 집합
  - `userNo` + `List<ExternalPrincipal>`
  - 동일 provider 중복 등록 방지 (RegistrationException.authProviderExists)

#### Value Objects
- **`ExternalPrincipal`** — `record(AuthProvider authProvider, String sub)` — OIDC의 `sub` 클레임
- **`ExternalPrincipal`**의 `AuthProvider`: `KAKAO` (kauth.kakao.com) / `NAVER` (nid.naver.com)

#### Commands / Queries
- **`ExternalAuthCreateCommand`** — 외부 인증 생성
- **`ExternalSubQuery`** — sub로 인증 조회
- **`UserExternalAuthQuery`** — 사용자 외부 인증 목록 조회

#### Repository
- **`ExternalAuthRepository`**

---

### 3. Device BC (디바이스)

**도메인 용어**: 디바이스, 기기, 기기 등록, FCM 토큰, 푸시 토큰, 디바이스 OS

**핵심 패키지**: `com.barlow.core.domain.device`

#### Aggregate Root
- **`Device`** — 푸시 알림을 위한 사용자 기기 정보
  - `userNo`: 소유자
  - `deviceId`: 기기 고유 식별자
  - `deviceOs`: `DeviceOs` enum (IOS / ANDROID)
  - `deviceToken`: FCM 푸시 토큰
  - `Status`: `ACTIVE` / `INACTIVE`
  - `modifyToken(newToken)` — 토큰 갱신 (불변 객체 반환)

#### Commands / Queries
- **`DeviceRegisterCommand`** — 디바이스 등록
- **`DeviceQuery`** — 디바이스 조회

#### Repository
- **`DeviceRepository`** — save / readOrNull / update / deleteById / findAllByUserNo

---

### 4. BillPost BC (법안)

**도메인 용어**: 법안, 법안 게시물, 의안, 발의안, 입법안, 오늘의 법안, 예고 법안, 입법예고, 최근 법안

**핵심 패키지**: `com.barlow.core.domain.billpost`

> 주의: "법안"과 "법안 게시물"은 이 시스템에서 동일한 개념이다 (BillPost = 법안 그 자체).

#### Aggregate Root
- **`BillPost`** — 법안의 모든 정보를 담는 핵심 객체
  - `BillInfo` — `record(billId, billName)` — 법안 식별 정보
  - `ProposerInfo` — `record(ProposerType type, String proposers)` — 발의자 정보
  - `LegislationInfo` — `record(LegislationType legislativeBody, ProgressStatus legislationProcessStatus)` — 소관위 + 진행상태
  - `summary` — AI 요약
  - `detail` — 상세 내용
  - `viewCount` — 조회수
  - `billProposers` — `List<BillProposer>` — 의원별 발의자 상세
  - `PreAnnouncementInfo` — `record(linkUrl, deadline)` — 입법예고 정보 (null 가능)
  - `calculateDeadlineDay(now)` — 입법예고 마감까지 남은 일수

#### Value Objects
- **`BillProposer`** — `record(proposerCode, proposerName, partyName, profileImagePath)` — 개별 발의 의원
- **`BillPostFilterTag`** — 법안 목록 필터 조건 (VO)
  - `legislationTypeTags`: 소관위 필터
  - `progressStatusTags`: 진행상태 필터
  - `proposerTypeTags`: 발의유형 필터
  - `partyNameTags`: 정당 필터
  - `isPreAnnouncement`: 입법예고 여부

#### Read Models
- **`BillPostsStatus`** — `record(List<BillPost> billPosts, boolean isLastPage)` — 페이지네이션 포함 목록
- **`TodayBillPostThumbnail`** — `record(billId, billName, proposers, createdAt)` — 홈 화면용 썸네일

#### Queries
- **`BillPostQuery`** — 법안 목록 조회 조건
- **`BillPostDetailQuery`** — 법안 상세 조회 조건

#### Exceptions
- **`BillPostDomainException`**

#### Repository
- **`BillPostRepository`** — retrieveRecentBillPosts / retrieveRecentBillPost / updateViewCount
- **`TodayBillPostRepository`** — 오늘의 법안 저장소

#### API 레이어 컨트롤러 구분
| 화면/API | 컨트롤러 | 경로 |
|---|---|---|
| 최근 법안 목록/상세 | `RecentBillRetrieveController` | `/v1/recent-bills` |
| 입법예고 법안 목록/상세 | `PreAnnounceBillRetrieveController` | `/v1/pre-announcement-bills` |
| 위원회별 법안 목록/상세 | `LegislationAccountBillPostRetrieveController` | `/v1/legislation-accounts/{legislationType}/bill-posts` |

---

### 5. LegislationAccount BC (입법계정)

**도메인 용어**: 입법계정, 위원회 계정, 위원회, 상임위, 소관위, 국회 계정, 정부 계정

**핵심 패키지**: `com.barlow.core.domain.legislationaccount`

> "입법계정"은 국회 각 위원회(또는 정부/의장)를 앱 내에서 "계정(Account)"처럼 표현한 개념이다.
> 사용자는 입법계정을 구독하고, 구독한 계정의 법안 알림을 받는다.

#### Aggregate Root
- **`LegislationAccount`** — 위원회/정부의 앱 내 계정 표현
  - `no`: 식별자
  - `type`: `LegislationType` — 어떤 위원회인지
  - `description`: 설명
  - `postCount`: 법안 게시물 수
  - `subscriberCount`: 구독자 수
  - `isSubscribed`: 현재 사용자의 구독 여부 (with 패턴으로 설정)
  - `isNotifiable`: 현재 사용자의 알림 여부 (with 패턴으로 설정)
  - `withSubscribed(boolean)` / `withNotifiable(boolean)` — 불변 객체 패턴

#### Read Models
- **`MyLegislationAccount`** — `record(no, bodyType, iconImagePath)` — 내가 구독한 입법계정 목록

#### Repository
- **`LegislationAccountRepository`** — 입법계정 저장소
- **`MyLegislationAccountRepository`** — 내 구독 입법계정 저장소

#### Service 계층 (`core.service.legislationaccount`)
- `LegislationAccountRetrieveService` — 입법계정 조회
- `LegislationAccountSubscribeService` — 구독/구독취소
- `LegislationAccountReader` — Reader 컴포넌트
- `LegislationAccountSubscriptionManager` — 구독 관리 컴포넌트

---

### 6. Subscribe BC (구독)

**도메인 용어**: 구독, 구독 취소, 팔로우, 팔로우 취소

**핵심 패키지**: `com.barlow.core.domain.subscribe`

> 주의: 패키지명은 `subscribe`이지만 핵심 클래스명은 `Subscription`이다.

#### Aggregate Root
- **`Subscription`** — 사용자와 입법계정 간의 구독 관계
  - `subscriberNo`: 구독하는 사용자의 userNo
  - `SubscriptionInfo.subscribeAccountNo`: 구독 대상 입법계정 번호
  - `SubscriptionInfo.subscribeAccountType`: `LegislationType`
  - `SubscriptionInfo.isSubscribed`: 구독 활성 여부
  - `activate()` / `deactivate()` — 구독/구독취소 (불변 객체 반환)

#### Exceptions
- **`SubscriptionDomainException`** — alreadySubscribed / alreadyUnSubscribed

#### Repository
- **`SubscriptionRepository`**

#### Service 계층 (`core.service.subscribe`)
- `SubscriptionActivator` — 구독/취소 처리 컴포넌트
- `SubscriptionReader` — 구독 조회 컴포넌트
- `SubscriptionWithdrawalHandler` — 탈퇴 시 구독 처리

---

### 7. NotificationSetting BC (알림 설정)

**도메인 용어**: 알림 설정, 알림 on/off, 푸시 알림 설정, 토픽 알림

**핵심 패키지**: `com.barlow.core.domain.notificationsetting`

> 주의: "알림 설정"(NotificationSetting)과 "알림함"(NotificationCenter)은 서로 다른 BC이다.

#### Aggregate Root
- **`NotificationSetting`** — 사용자의 특정 토픽에 대한 알림 설정
  - `userNo`: 사용자
  - `notificationTopic`: `NotificationTopic` — 어떤 토픽의 알림인지
  - `isNotifiable`: 알림 활성 여부
  - `activate()` / `deactivate()` — 알림 켜기/끄기 (불변 객체 반환)

#### Exceptions
- **`NotificationSettingDomainException`**

#### Repository
- **`NotificationSettingRepository`**

#### Queries
- **`LegislationNotificationSettingQuery`** — 위원회 알림 설정 조회

#### Service 계층 (`core.service.notificationsetting`)
- `NotificationSettingService` — 알림 설정 Business 서비스
- `NotificationSettingActivator` — 알림 활성화 컴포넌트
- `NotificationSettingReader` — 알림 설정 조회 컴포넌트
- `NotificationWithdrawalHandler` — 탈퇴 시 알림 설정 처리

---

### 8. NotificationCenter BC (알림함)

**도메인 용어**: 알림함, 알림 수신함, 알림 센터, 수신된 알림, 푸시 알림 내역

**핵심 패키지**: `com.barlow.core.domain.notificationcenter`

> "알림 설정"(사용자가 어떤 알림을 받을지 설정)과 달리,
> "알림함"은 실제로 수신된 알림 메시지의 목록이다.

#### Read Model
- **`NotificationCenterItem`** — `record(billId, notificationTopic, title, body, createdAt)` — 수신된 알림 항목

#### Repository
- **`NotificationCenterItemRepository`** — 알림 저장
- **`MyNotificationCenterRepository`** — 내 알림 목록 조회

---

### 9. Reaction BC (리액션)

**도메인 용어**: 리액션, 반응, 좋아요, 싫어요, 흠, 공감

**핵심 패키지**: `com.barlow.core.domain.reaction`

#### Aggregate Root
- **`Reaction`** — 사용자의 특정 대상에 대한 반응
  - `userNo`: 반응한 사용자
  - `targetId`: 반응 대상 ID (billId 또는 commentId)
  - `targetType`: `ReactionTarget` — `BILL_POST` / `COMMENT`
  - `reactionType`: `ReactionType` — `LIKE`(좋아요) / `DISLIKE`(싫어요) / `HMM`(흠)

#### Read Models
- **`ReactionStatus`** — 반응 현황 (현재 사용자의 반응 상태 포함)

#### Exceptions
- **`ReactionDomainException`**

#### Queries
- **`ReactionQuery`** — 리액션 조회 조건

#### Repository
- **`ReactionRepository`**

#### Service 계층 (`core.service.reaction`)
- `ReactionService` — 리액션 Business 서비스
- `ReactionProcessor` — 리액션 처리 컴포넌트
- `ReactionReader` — 리액션 조회 컴포넌트

---

### 10. Version BC (버전)

**도메인 용어**: 클라이언트 버전, 앱 버전, 최소 버전, 강제 업데이트, 업데이트 권장

**핵심 패키지**: `com.barlow.core.domain.version`

#### Domain Objects
- **`ClientVersionPolicy`** — 버전 정책 (deviceOs별 최소/최신 버전 관리)
  - `evaluate(clientVersion)` → `ClientVersionStatus` 반환
  - `NEED_FORCE_UPDATE`: 최소 버전 미만 → 강제 업데이트
  - `UPDATE_AVAILABLE`: 최신 버전 미만 → 업데이트 권장
  - `LATEST`: 최신 버전

- **`SemanticVersion`** — VO (major.minor.patch-suffix)
  - 패턴: `^\d+\.\d+\.\d+(-[a-zA-Z0-9]+)?$`
  - `isLessThan(other)`, `isOfficialRelease()`

#### Exceptions
- **`ClientVersionException`** — invalidVersion

#### Queries
- **`ClientVersionQuery`**

#### Repository
- **`ClientVersionRepository`**

---

## Cross-cutting 개념

### Passport (여권)

**도메인 용어**: 여권, 인증 정보, 요청자 컨텍스트, 로그인 컨텍스트

- 위치: `com.barlow.core.domain.Passport` (루트 패키지)
- **의미**: 인증된 HTTP 요청의 실행 주체를 나타내는 불변 객체. Security 계층에서 생성되어 Controller로 전달됨.
- 구성:
  - `user`: `User` — 요청한 사용자
  - `device`: `Passport.Device` — 요청한 디바이스 (deviceId, osVersion, DeviceOs)
- 핵심 메서드:
  - `isAtLeastGuest()` — GUEST 이상 여부
  - `isForGuest()` — GUEST 전용 여부
  - `isValidOs()` — IOS 또는 ANDROID 여부

---

## 전체 Enum 정의

### `LegislationType` — 소관위/입법 기관 유형

| Enum 값 | 한국어 명칭 | 비고 |
|---|---|---|
| `HOUSE_STEERING` | 국회운영위원회 | |
| `LEGISLATION_AND_JUDICIARY` | 법제사법위원회 | |
| `NATIONAL_POLICY` | 정무위원회 | |
| `STRATEGY_AND_FINANCE` | 기획재정위원회 | |
| `EDUCATION` | 교육위원회 | |
| `SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS` | 과학기술정보방송통신위원회 | |
| `FOREIGN_AFFAIRS_AND_UNIFICATION` | 외교통일위원회 | |
| `NATIONAL_DEFENSE` | 국방위원회 | |
| `PUBLIC_ADMINISTRATION_AND_SECURITY` | 행정안전위원회 | |
| `CULTURE_SPORTS_AND_TOURISM` | 문화체육관광위원회 | |
| `AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES` | 농림축산식품해양수산위원회 | |
| `TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS` | 산업통상자원중소벤처기업위원회 | |
| `HEALTH_AND_WELFARE` | 보건복지위원회 | |
| `ENVIRONMENT_AND_LABOR` | 환경노동위원회 | |
| `LAND_INFRASTRUCTURE_AND_TRANSPORT` | 국토교통위원회 | |
| `INTELLIGENCE` | 정보위원회 | |
| `GENDER_EQUALITY_FAMILY` | 여성가족위원회 | |
| `SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS` | 예산결산특별위원회 | |
| `GOVERNMENT` | 정부 | 위원회 아님 |
| `SPEAKER` | 국회의장 | 위원회 아님 |
| `SPECIAL_COMMITTEE` | 특별위원회 | fallback |
| `EMPTY` | 소관위미접수상태 | 미배정 상태 |

### `ProgressStatus` — 입법 진행 상태 (한국어 → Enum)

| Enum 값 | 한국어 값 (DB/API) | 의미 |
|---|---|---|
| `RECEIVED` | 접수 | 최초 접수 (알림 발송 기준점) |
| `COMMITTEE_RECEIVED` | 소관위접수 | 소관위 배정 완료 |
| `COMMITTEE_REVIEW` | 소관위심사 | 소관위 심사 중 |
| `REPLACED_AND_DISCARDED` | 대안반영폐기 | 다른 법안에 통합 폐기 |
| `SYSTEMATIC_WORDING_REVIEW` | 체계자구심사 | |
| `PLENARY_SUBMITTED` | 본회의부의안건 | 본회의 상정 |
| `PLENARY_DECIDED` | 본회의의결 | 본회의 통과 |
| `WITHDRAWN` | 철회 | |
| `GOVERNMENT_TRANSFERRED` | 정부이송 | |
| `REDEMAND_REQUESTED` | 재의요구 | 대통령 거부권 |
| `REJECTED` | 재의(부결) | |
| `PROMULGATED` | 공포 | 법률 확정 |
| `ABROGATE` | 폐기 | fallback |

### `ProposerType` — 발의 유형

| Enum 값 | 한국어 값 |
|---|---|
| `GOVERNMENT` | 정부 |
| `CHAIRMAN` | 위원장 |
| `SPEAKER` | 의장 |
| `LAWMAKER` | 의원 |
| `ETC` | 기타 |

### `ReactionType` — 리액션 타입

| Enum 값 | 한국어 의미 |
|---|---|
| `LIKE` | 좋아요 |
| `DISLIKE` | 싫어요 |
| `HMM` | 흠 (중립적 반응) |

### `ReactionTarget` — 리액션 대상

| Enum 값 | 대상 |
|---|---|
| `BILL_POST` | 법안 게시물 |
| `COMMENT` | 댓글 |

### `AuthProvider` — 소셜 로그인 제공자

| Enum 값 | issuer |
|---|---|
| `KAKAO` | https://kauth.kakao.com |
| `NAVER` | https://nid.naver.com |

### `NotificationTopic` — 알림 토픽 (25개)

- **위원회 알림** (0~17번 ordinal): LegislationType과 1:1 대응
- **진행상태 알림** (18~22번): RECEIPT / SUBMISSION_PLENARY_SESSION / RESOLUTION_PLENARY_SESSION / RECONSIDERATION_GOVERNMENT / PROMULGATION
- **사용자 상호작용** (23~24번): REACTION / COMMENT

### `ClientVersionStatus` — 버전 상태

| Enum 값 | 의미 |
|---|---|
| `LATEST` | 최신 버전 |
| `UPDATE_AVAILABLE` | 업데이트 권장 |
| `NEED_FORCE_UPDATE` | 강제 업데이트 필요 |

### `DeviceOs` — 디바이스 OS

| Enum 값 | 의미 |
|---|---|
| `IOS` | 애플 iOS |
| `ANDROID` | 안드로이드 |

---

## 레이어별 네이밍 규칙

### core:domain 계층
| 역할 | 네이밍 패턴 | 예시 |
|---|---|---|
| Aggregate Root | `{명사}` (AR suffix 없음) | `User`, `Device`, `Subscription` |
| Value Object | `{명사}` or record | `BillProposer`, `ExternalPrincipal` |
| 읽기 전용 모델 | `My{명사}`, `{명사}Status`, `{명사}Thumbnail` | `MyLegislationAccount`, `BillPostsStatus` |
| 명령 객체 | `{동사}{명사}Command` | `UserRegisterCommand`, `DeviceRegisterCommand` |
| 조회 객체 | `{명사}Query` | `BillPostQuery`, `UserQuery` |
| 저장소 인터페이스 | `{명사}Repository` | `UserRepository`, `BillPostRepository` |
| 도메인 예외 | `{BC명}DomainException` | `AccountDomainException`, `ReactionDomainException` |
| 필터/정책 | `{명사}FilterTag`, `{명사}Policy` | `BillPostFilterTag`, `TermsPolicy` |

### core:service 계층
| 역할 | 네이밍 패턴 | 예시 |
|---|---|---|
| Business 서비스 (@Service) | `{명사}{동사}Service` | `AccountCreateService`, `ReactionService` |
| 조합 파사드 | `{명사}Facade` | `HomeRetrieveFacade`, `MenuFacade` |
| Reader 컴포넌트 (@Component) | `{명사}Reader` | `BillPostReader`, `UserReader` |
| Handler 컴포넌트 | `{명사}Handler` | `LegislationAccountWithdrawalHandler` |
| Activator 컴포넌트 | `{명사}Activator` | `SubscriptionActivator`, `NotificationSettingActivator` |
| Manager 컴포넌트 | `{명사}Manager` | `LegislationAccountSubscriptionManager`, `TermManager` |
| Processor 컴포넌트 | `{명사}Processor` | `ReactionProcessor`, `GuestUserWithdrawalProcessor` |

### infra:storage 계층
| 역할 | 네이밍 패턴 | 예시 |
|---|---|---|
| JPA 엔티티 | `{명사}JpaEntity` | `UserJpaEntity`, `BillPostJpaEntity` |
| JPA 저장소 | `{명사}JpaRepository` | `UserRepositoryJpaRepository` |
| 도메인 저장소 구현체 | `{명사}RepositoryAdapter` | `UserRepositoryAdapter`, `BillPostRepositoryAdapter` |
| JPA Specification | `{명사}Specifications` | `BillPostSpecifications` |

---

## BC 간 의존 관계

```
Account BC
  └── ExternalAuth BC  (UserExternalAuth가 ExternalPrincipal을 통해 AuthProvider 사용)
  └── Term Sub-BC      (회원가입 시 TermsPolicy 검증)

BillPost BC
  └── LegislationType  (소관위 정보)
  └── ProgressStatus   (진행 상태)
  └── ProposerType     (발의 유형)
  └── PartyName        (정당)

LegislationAccount BC
  └── LegislationType  (어떤 위원회인지)
  └── Subscribe BC     (구독 여부 with 패턴으로 조합)
  └── NotificationSetting BC (알림 여부 with 패턴으로 조합)

Subscribe BC
  └── LegislationType  (구독 대상 위원회 유형)

NotificationSetting BC
  └── NotificationTopic (알림 토픽)

NotificationCenter BC (알림함)
  └── NotificationTopic (수신된 알림의 토픽)
  └── BillPost BC      (billId 참조)

Reaction BC
  └── BillPost BC      (BILL_POST 대상)

Passport (Cross-cutting)
  └── User (Account BC의 AR)
  └── Device BC
```

### 주요 오해 방지 노트

| 혼동하기 쉬운 용어 | 실제 코드 | 설명 |
|---|---|---|
| "구독 클래스" | `Subscription` | 패키지는 `subscribe`, 클래스는 `Subscription` |
| "위원회" | `LegislationType` enum | AR이 아님, enum 값임 |
| "입법계정" | `LegislationAccount` | 위원회를 앱 내 계정으로 추상화한 AR |
| "알림" (설정) | `NotificationSetting` | 사용자가 알림을 받을지 설정하는 것 |
| "알림" (수신함) | `NotificationCenterItem` | 실제로 수신된 알림 메시지 |
| "법안" | `BillPost` | 법안과 게시물 개념이 통합된 AR |
| "의원" | `LawmakerJpaEntity` | 도메인 AR 없음, infra:storage에만 존재 |
| "입법예고" | `BillPost.PreAnnouncementInfo` | BillPost 내부의 중첩 record |