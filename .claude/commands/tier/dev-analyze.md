# /dev-analyze

Tier 3 아키텍처 커맨드 실행 전 사전 스크리닝.

이슈 또는 요구사항을 분석하여 **기존 개선(Redesign)** 인지 **신규 구축(New Design)** 인지 판별하고,
적합한 커맨드(`/arch-redesign` 또는 `/arch-design`)에 필요한 입력 초안을 도출한다.
코드를 작성하지 않는다.

---

## 실행 순서

### Step 1 — 이슈 이해

이슈 본문, 제목, 코멘트를 읽고 다음을 파악한다:
- 현재 어떤 문제가 있는가 (또는 어떤 시스템을 새로 만들어야 하는가)
- 왜 이것이 단순 구현이 아닌 아키텍처 수준의 결정인가
- 관련된 BC, 모듈, 계층이 어디인가

### Step 2 — 트랙 판별 (Redesign vs New Design)

아래 기준으로 어느 트랙에 해당하는지 판별한다.

**Redesign 트랙 (`/arch-redesign`) 신호:**
- 요구사항이 "개선", "성능 문제", "리팩토링", "버그", "병목" 등 기존 시스템 변경을 가리킴
- 분석 대상 파일/패키지가 코드베이스에 이미 존재함
- 보존해야 할 API 계약 또는 DB 스키마가 존재함

**New Design 트랙 (`/arch-design`) 신호:**
- 요구사항이 "신규 구축", "처음부터", "새 시스템", "없던 기능을 만든다" 등을 가리킴
- 해당 BC나 패키지가 코드베이스에 존재하지 않거나 빈약한 더미 수준
- 분석할 AS-IS 코드가 없거나 아주 빈약하여 7-Lens 비판 대상이 되지 않음

판별이 명확하지 않으면 "더 보수적인 트랙"(New Design)으로 분류하고 이유를 명시한다.

### Step 3 — 입력 초안 도출

판별 결과에 따라 해당 커맨드의 입력 초안을 작성한다.

---

#### [Redesign 트랙] `/arch-redesign` 입력 초안

```
[BUSINESS_GOAL 초안]
이 시스템/기능이 달성해야 할 핵심 비즈니스 목표.
성능 수치, 정합성 요구, 사용자 경험 목표 등을 포함한다.
예) "구독 이벤트를 피크 TPS 500 이상으로 처리하고, 알림 발송 멱등성을 보장한다"

[AS_IS_SCOPE 초안]
재설계 대상 파일/패키지 경로 목록.
예)
  core/domain/subscribe
  core/service/subscribe
  infra/storage (SubscriptionJpaEntity, SubscriptionRepositoryAdapter)

[FROZEN_ZONE 초안]
절대 변경 불가 항목과 그 이유.
이슈에 명시되어 있거나, 기존 API 계약·DB 스키마에서 추론한다.
예)
  - subscription 테이블 스키마 (데이터 마이그레이션 비용)
  - POST /v1/subscriptions API 계약 (클라이언트 배포 주기)
```

---

#### [New Design 트랙] `/arch-design` 입력 초안

```
[BUSINESS_GOAL 초안]
이 시스템이 달성해야 할 핵심 비즈니스 목표 + NFR.
성능 수치, 정합성 요구, SLA 등을 포함한다.
예) "선착순 1만 명 동시 결제, 이중 결제 0건, P99 레이턴시 2초 이내"

[DOMAIN_BOUNDARIES 초안]
설계 대상 BC(Bounded Context) 목록.
이슈나 요구사항 문서에서 추론한 경계.
예) Payment, Order, Inventory

[EXTERNAL_DEPENDENCIES 초안]
연동해야 할 외부 시스템 목록.
레거시 시스템, 외부 API, 기존 DB 등을 명시한다.
예) KakaoPay API, 레거시 주문 DB, 회원 서비스
```

---

불확실한 항목은 `[불확실 — 확인 필요]`로 표시한다.

### Step 4 — 초안 출력 및 확인 요청

다음 형식으로 채팅에 출력한다:

```
## /dev-analyze 사전 스크리닝 결과

### 이슈 요약
{이슈가 다루는 문제 2~3줄}

### 아키텍처 수준 결정이 필요한 이유
{단순 구현이 아닌 이유 1~3줄}

### 판별 결과: {Redesign 트랙 / New Design 트랙}
{판별 근거 1~2줄}

---

### {/arch-redesign 또는 /arch-design} 입력 초안

**{변수명 1}**
{초안}

**{변수명 2}**
{초안}

**{변수명 3}**
{초안}

---

위 초안으로 `{커맨드명}`을 실행하시겠습니까?
- 수정이 필요하면 원하는 항목을 알려주세요.
- 이대로 진행하려면 "진행해"라고 말씀해 주세요.
- 취소하려면 "취소"라고 말씀해 주세요.
```

### Step 5 — 사용자 응답 처리

- **"진행해"**: 확정된 입력으로 해당 커맨드(`/project:pipeline:arch-redesign` 또는 `/project:pipeline:arch-design`) 실행
- **수정 요청**: 해당 항목 수정 후 Step 4 재출력
- **"취소"**: 종료. 추가 행동 없음