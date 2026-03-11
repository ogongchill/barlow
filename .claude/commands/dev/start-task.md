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

에이전트가 반환하는 4개 섹션 분석 결과를 수신한다:
1. Target Scope & Entry Point
2. Core Structure Snippets
3. Execution Flow & State
4. Constraints & Risks

### 6. plan.md 작성

code-analyzer 분석 결과를 바탕으로 `.claude/workspace/plan.md`를 작성한다.

```markdown
# 구현 계획 — #{번호} {제목}

## 변경 범위
- [ ] `{파일경로}`: {변경 내용 한 줄}

## 설계 결정
- {결정 사항}: {선택 이유}

## 리스크
- {code-analyzer Constraints & Risks 섹션 기반}

## 커밋 계획
1. `feat : {내용}` — {대상 파일}
2. `test : {내용}` — {대상 파일}
3. `lint : lint 적용`
```

MEMORY.md `# currentWork` 섹션을 작성한다.

### 7. 종료 신호 출력

plan.md 작성이 완료되면 마지막 줄에 아래를 출력하고 종료한다:

```
PLAN_READY: {type}/issue/[ISSUE_NUMBER]
```

Worker가 이 신호를 감지하여 Slack 전송 및 승인 대기로 전환한다.
