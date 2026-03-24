---
description: 코드베이스를 최대한 깊이 분해·이해·어떻게 동작하는지 분석하고 사용자와 대화를 통해 구현 계획을 확정한 뒤 docs/plans/에 저장한다. 단일 요구사항 및 대규모 재설계 후 개발 모두에 사용한다.
model: opus
---

# /arch:plan

> **단계 명칭**: 이 커맨드의 계획 수립 단계는 **Step N**으로 지칭한다. /arch:redesign의 Stage N과 구별된다.
> 계획 결과물(docs/plans/)의 실행 항목은 **Task N**으로 표기한다.

## 실행 모드

이 커맨드는 두 가지 모드로 동작한다. `[ADR]` 파라미터 유무로 자동 판별한다.

### 모드 A — 독립 계획 수립 (ADR 없음)
**진입 경로**: 개발자 직접 호출
- Step 1에서 설계 결정을 포함한 깊은 분석 수행
- Step 2 첫 번째 피드백 루프에서 FROZEN_ZONE을 사용자와 확인
- 탐색적 성격: 여러 구현 방향을 검토하고 확정

### 모드 B — ADR 기반 구현 계획 (ADR 있음)
**진입 경로**: /arch-redesign 완료 후
- Step 0에서 ADR의 FROZEN_ZONE·채택 패러다임·영향 파일 추출
- Step 1 분석은 ADR 제약 확인 중심 (설계 결정은 ADR에서 이미 완료)
- 실행 지향적: ADR 방향에서 벗어나는 Task 제안 금지

---

<input_variables>

| 변수 | 필수 | 설명 | 예시 |
|------|------|------|------|
| `[TOPIC]` | 필수 | 개발 주제 (kebab-case) | `order-expiry-batch-limit` |
| `[ADR]` | 선택 | 재설계 후 개발 시 ADR 파일 경로 | `docs/adr/ARCH-001-2026-03-03-order-redesign.md` |

입력이 없으면 먼저 질문하여 확보한 뒤 Step 0으로 진행한다.

</input_variables>

---

<execution_rules>

## Step 0 — 범위 추론 및 확인

> **목적**: 무엇을 얼마나 건드릴 것인지를 사용자와 합의한다.

### Sub-step 1 — ADR 로드 (있는 경우)

`[ADR]` 경로가 주어진 경우 파일을 읽고 다음 항목을 추출한다:

- **FROZEN_ZONE**: 변경 불가 항목 목록
- **선택 패러다임**: 채택된 아키텍처 패턴 (Saga, Outbox 등)
- **영향받는 파일/클래스**: ADR의 "결과" 섹션

### Sub-step 2 — 범위 추론

사용자 요청과 ADR(있는 경우)을 바탕으로 영향 scope를 추론한다.

```
[추론된 범위]
- 진입점  : {외부 요청 시작점, e.g. OrderController.createOrder}
- 핵심 클래스 : {예상 변경 대상 클래스 목록}
- 레이어  : {api / service / domain / storage 중 해당}
- 제외 범위 : {FROZEN_ZONE 또는 영향 없다고 판단한 항목}

이 범위가 맞나요? 추가하거나 제외할 항목이 있으면 말씀해 주세요.
```

> ⚠️ 사용자 확인을 받은 후에만 Step 1로 진행한다.

---

## Step 1 — 코드베이스 분해

> **목적**: 확정된 scope 내 코드를 있는 그대로 이해한다. 이 단계에서는 판단하지 않는다.
>
> **모드 A (ADR 없음)**: 설계 문제와 개선 기회까지 파악하는 깊은 분석. AS-IS의 한계를 식별한다.
> **모드 B (ADR 있음)**: ADR 제약 적용 위치와 영향 파일 확인 중심. 설계 재검토 금지.

확정된 scope의 모든 파일을 읽는다. 다음 항목을 템플릿에 따라 도출한다:

### 출력 템플릿

```
## 코드베이스 분해 결과

### 책임 목록
| 클래스/메서드 | 역할 (한 줄) | 레이어 |
|---|---|---|
| ... | ... | ... |

### 의존성 방향
A → B 형태로 전체 열거 (import, 주입, 호출 포함)
- ...

### 상태변이 지점
| 상태 | 전이 조건 | 변이 위치 (파일:라인) |
|---|---|---|
| ... | ... | ... |

### 트랜잭션 경계
| 메서드 | 시작 | 종료 | 경계 내 외부 호출 |
|---|---|---|---|
| ... | ... | ... | ... |

### ADR 제약 오버레이 (ADR 있는 경우)
- FROZEN_ZONE: {항목}
- 채택 패러다임: {패턴}이 적용될 지점: {위치}
```

분해 완료 후 Step 2로 진행한다.

---

## Step 2 — 구현 계획 수립

> **목적**: 비즈니스 흐름을 기준으로 구현 Task를 분해하고, 사용자와 대화를 통해 점진적으로 개선하여 확정한다.

### Task 분해 기준

- **단위**: 단일 기술 레이어가 아닌 비즈니스 흐름 단위
  - 예) "주문 생성 → 재고 예약 → 결제 요청" 을 각 Task로
- **크기**: 한 번의 구현 세션으로 완결 가능한 수준

### 계획 초안 형식

```
## 구현 계획 초안: {TOPIC}

**변경 불가 항목 (FROZEN_ZONE)**
- {항목}

**Task 목록**

### Task 1: {비즈니스 흐름 명칭}
- 대상: {파일/클래스}
- 변경: {구체적인 변경 내용}
- 하네스 체크포인트:
  - [ ] {Architecture 규칙}
  - [ ] {Error Handling 규칙}
  - [ ] {Testing 규칙}
- FROZEN_ZONE 위반: 없음 / {위반 항목}

### Task 2: ...

---
피드백이 있으시면 말씀해 주세요. 수정 후 재제시합니다.
확정하시려면 "확정"이라고 말씀해 주세요.
```

### 모드별 주의사항

- **모드 A**: 첫 번째 계획 초안 제시 전에 FROZEN_ZONE을 사용자와 합의한다.
- **모드 B**: ADR의 FROZEN_ZONE에서 벗어나는 Task는 제안하지 않는다. ADR 방향과 충돌 시 사용자에게 알리고 대기.

### 반복 규칙

- 사용자 피드백 → 계획 수정 → 재제시를 "확정" 신호가 올 때까지 반복한다.
- 변경이 없는 재제시는 하지 않는다.

---

## Step 3 — 계획 파일 저장

> **목적**: 확정된 계획을 세션 간 유지 가능한 파일로 저장한다.

사용자가 "확정"하면 아래 경로에 파일을 생성한다.

**파일 경로**: `docs/plans/{YYYY-MM-DD}-{TOPIC}.md`

### 저장 파일 형식

```markdown
# 구현 계획: {TOPIC}

**날짜**: {YYYY-MM-DD}
**상태**: 진행 중  ← Task 완료 시 업데이트
**연관 ADR**: {ADR 경로 또는 없음}

---

## 변경 불가 항목 (FROZEN_ZONE)
- {항목}

## 코드베이스 분해 요약
{Step 1 출력 요약}

## Task 목록

- [ ] Task 1: {명칭}
- [ ] Task 2: {명칭}
- ...

---

## Task 상세

### Task 1: {명칭}
**상태**: 대기 / 진행 중 / 완료

- 대상: {파일/클래스}
- 변경: {내용}
- 구현 전 하네스 가이드 로드:
  - `.claude/skills/coding-rules/resources/ARCHITECTURE.md`
  - `.claude/skills/coding-rules/resources/ERROR_HANDLING.md`
  - `.claude/skills/coding-rules/resources/TESTING.md`
- 하네스 체크포인트:
  - [ ] ...
- 완료 조건: `/harness-check` FAIL 0개

### Task 2: ...
```

파일 생성 후 경로와 다음 단계를 사용자에게 알린다:

```
계획이 저장되었습니다: docs/plans/{YYYY-MM-DD}-{TOPIC}.md

다음 단계:
→ Task 1 구현을 시작하려면 "Task 1 구현" 이라고 말씀해 주세요.
   구현 완료 후 /harness-check 로 검증합니다.
```

> **Task 구현 진입 시 규칙 로드 (필수)**: "Task N 구현" 트리거를 받으면 코드 작성 전에
> 반드시 아래 파일을 읽는다. 새 세션이거나 컨텍스트 압축 후 재진입하는 경우를 포함한다.
>
> ```
> 1. docs/DOMAIN_ENCYCLOPEDIA.md          — 도메인 용어 확인
> 2. .claude/skills/coding-rules/resources/ARCHITECTURE.md
> 3. .claude/skills/coding-rules/resources/DOMAIN_RULES.md   (도메인 변경 포함 시)
> 4. .claude/skills/coding-rules/resources/SERVICE_RULES.md  (서비스 변경 포함 시)
> 5. .claude/skills/coding-rules/resources/ERROR_HANDLING.md (예외 추가 포함 시)
> ```
> Task 상세의 "하네스 체크포인트"를 확인해 해당 항목에 맞는 파일만 선택적으로 로드한다.

</execution_rules>

```