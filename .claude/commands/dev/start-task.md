---
description: GitHub 이슈를 분석하고 작업 브랜치를 생성한 뒤 plan.md를 작성한다
---

# /dev:start-task

## 입력

`[ISSUE_NUMBER]`: GitHub Issue 번호 (필수)

입력이 없으면 즉시 중단한다:
```
사용법: /dev:start-task {issue_number}
예시:   /dev:start-task 120
```

---

## 실행 순서

### 1. 사전 확인

```bash
git status
```

uncommitted changes가 있으면 즉시 중단한다:
```
[start-task] 중단: uncommitted changes가 있습니다.
커밋 또는 stash 후 다시 실행하세요.
```

### 2. develop 최신화

```bash
git checkout develop
git pull origin develop
```

### 3. GitHub Issue 분석

```bash
gh issue view [ISSUE_NUMBER] --json title,body,labels,assignees
```

아래 항목을 추출한다:
- 요구사항 핵심 요약 (3줄 이내)
- 영향 BC — `docs/DOMAIN_ENCYCLOPEDIA.md` 해당 섹션 참조
- 작업 유형: `feat` / `fix` / `refactor` / `batch`
- 예상 영향 레이어: `domain` / `service` / `infra` / `api` 중 해당

### 4. 브랜치 생성

```bash
git checkout -b {type}/issue/[ISSUE_NUMBER]
```

`{type}`은 Step 3에서 추출한 작업 유형(`feat` / `fix` / `refactor` 등)을 사용한다.

### 5. code-analyzer 서브에이전트 스폰

위 이슈 분석 결과를 바탕으로 `code-analyzer` 에이전트를 실행한다:

```
Agent("code-analyzer", prompt="""
BC: {영향 BC명 목록}
SCOPE: {이슈 요구사항 한 줄 요약}
LAYERS: {예상 영향 레이어}
""")
```

에이전트가 반환하는 4개 섹션 중 아래와 같이 활용한다:

| 섹션 | 활용 |
|---|---|
| 1. Target Scope & Entry Point | → 변경 범위 파일 목록 |
| 2. Core Structure Snippets | → 구현 스펙 힌트 (패턴이 불명확한 경우에만) |
| 3. Execution Flow & State | → 사용하지 않음 |
| 4. Constraints & Risks | → 리스크 & 제약 섹션 |

### 6. plan.md 작성

code-analyzer 분석 결과를 바탕으로 `.claude/workspace/plan.md`를 작성한다.

**템플릿:**

```markdown
# 구현 계획 — #{번호} {제목}

## 이슈 요약
{요구사항 핵심 1~2줄. 구현 중 엣지케이스 판단 기준으로 사용.}
- 영향 BC: {BC명}
- 변경 레이어: {domain / service / infra / api 중 해당만 나열}

---

## 변경 범위
{code-analyzer Target Scope 기반. 실제 손댈 파일만 나열.}

- `{파일경로}` (신규/수정): {한 줄}

---

## 구현 스펙
{해당 레이어만 포함. 없는 레이어 섹션은 생략.}

### core:domain

**{ClassName}.{method}()**
```
if {위반 조건} → throw {ExceptionClass}.{staticFactory}()
return new {ClassName}(..., {변경 필드}={값})
```
→ {힌트: 불필요하면 줄 삭제}

### core:service

**{ServiceClassName}.{method}({params}): {반환타입}**
- `@Transactional`
- {위임 흐름 1줄: e.g. reader.find() → domain.method() → manager.save()}
→ {힌트: 불필요하면 줄 삭제}

### infra:storage

- `{파일경로}`: {한 줄 설명}

### app:api

- `{파일경로}`: {한 줄 설명}

---

## 리스크 & 제약
{code-analyzer Constraints & Risks 기반. 해당 없는 항목은 줄 삭제.}

- **트랜잭션**: {경계 및 주의사항}
- **외부 인프라**: {Redis / FCM / 외부 API 연동 여부 및 주의사항}
- **BC 경계**: {다른 BC와의 연관 관계 및 주의사항}
- **기존 패턴 충돌**: {deprecated 메서드, 변경 시 영향받는 다른 BC}
- **기타**: {락, 비동기, 캐시 등}

---

## 구현 범위 외
{해당 없으면 섹션 전체 삭제.}

- `{파일/메서드/BC}`: {이유}
```

**작성 규칙:**

- **구현 스펙 상세도는 레이어별로 다르게 적용한다:**
  - `core:domain` → pseudo-code: 비즈니스 규칙, 예외 조건, 반환값을 명시
  - `core:service` → 시그니처 + `@Transactional` 여부 + 위임 흐름 1줄
  - `infra:storage`, `app:api` → 파일경로 + 한 줄 설명

- **힌트(`→`)는 아래 경우에만 작성한다:**
  - 메서드 네이밍이 여러 선택지일 때
  - code-analyzer 스니펫 scope 밖의 더 나은 참고 패턴이 있을 때
  - 기존 유사 사례가 없는 새 구조일 때
  - 그 외에는 줄 자체를 삭제한다

- **리스크 & 제약:** code-analyzer Constraints & Risks를 그대로 옮기되 구현과 무관한 항목은 삭제한다

- **구현 범위 외:** code-analyzer가 인접하다고 식별한 코드 중 이번 scope 밖인 것, BC 경계, API 계약 변경 금지 사항만 작성한다. 해당 없으면 섹션 전체를 작성하지 않는다

- **커밋 계획은 작성하지 않는다** — WORKFLOW-SHIP.md가 담당한다

MEMORY.md `# currentWork` 섹션을 작성한다.

### 7. 종료 신호 출력

plan.md 작성이 완료되면 아래를 출력하고 종료한다:

```
PLAN_READY: {type}/issue/[ISSUE_NUMBER]
```

Worker가 이 신호를 감지하여 Slack 전송 및 승인 대기로 전환한다.

Worker 없이 터미널에서 직접 진행하려면:
```
→ /dev:review-plan
```
