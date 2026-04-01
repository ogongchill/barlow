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

| BC | 담당 |
|----|------|
| `account` | 계정 생성·로그인·탈퇴, Guest → Member 전환 |
| `home` | 홈 화면 데이터 조합, 알림 센터 조회 |
| `legislationaccount` | 입법기관 계정 조회, 구독·구독 해제 |
| `billpost` | 법안 목록·상세 조회, 조회수 추적 |
| `notificationsetting` | 알림 설정 활성화·비활성화 |
| `reaction` | 반응 조회·추가·삭제 |
| `subscribe` | 구독 상태 관리 (다른 BC에서 주입받아 사용) |
| `menu` | 알림 설정 메뉴 조회·토글 |
| `version` | 클라이언트 버전 상태 검증 |

---

## 주요 패턴

### Facade

복수의 Service를 조합해 단일 진입점을 제공합니다.  
`HomeRetrieveFacade`(홈 화면), `MenuFacade`(메뉴)가 이 패턴을 사용합니다.

### 전략 패턴

회원 탈퇴 흐름은 Role(Guest/Member)에 따라 다른 `UserWithdrawalProcessor` 구현체로 분기합니다.

### 캐싱

자주 조회되는 법안 상세 데이터에 Caffeine 로컬 캐시를 적용합니다.

---

## 의존 모듈

```gradle
implementation project(":core:domain")
implementation 'com.github.ben-manes.caffeine:caffeine'
implementation 'org.springframework:spring-context-support'
compileOnly 'org.springframework:spring-context'
compileOnly 'org.springframework:spring-tx'
```
