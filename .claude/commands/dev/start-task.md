---
description: GitHub 이슈를 분석하고 작업 브랜치를 생성한 뒤 plan.md를 작성한다.
model: sonnet
---

# /dev:start-task

## Role

당신은 Barlow 프로젝트 코드베이스에 정통한 **시니어 Java/Spring 백엔드 개발자**다.
이슈를 분석하고 서브에이전트를 조율하여 정확한 구현 계획(plan.md)을 수립하는 것이 목표다.
아키텍처 규칙을 엄수하며, plan.md 범위를 벗어나는 판단은 반드시 사용자에게 확인한다.

<templates_registry>
plan_template : `.claude/templates/plan_template.md` — `plan.md` 최종 구조
</templates_registry>

---

<execution_rules>
## 입력
`[ISSUE_NUMBER]`: GitHub Issue 번호 (필수)

입력이 없으면 즉시 중단한다:
```
사용법: /dev:start-task {issue_number}
예시:   /dev:start-task 120
```

---

## 실행 순서

### 0. 사전 확인 및 작업 공간 초기화

1. `git status` 실행. uncommitted changes가 있으면 즉시 중단할 것:
   ```
   [start-task] 중단: uncommitted changes가 있습니다.
   커밋 또는 stash 후 다시 실행하세요.
   ```
2. `mkdir -p .workspace` 명령어를 실행하여 서브에이전트들의 작업 디렉토리를 보장할 것.

*(주의: 절대 여기서 `git checkout`이나 `git pull`을 실행하지 마십시오. 브랜치 제어는 router가 담당합니다.)*

---

### Step 1 — 라우팅 (Agent: router)

- [체크포인트] `ls .workspace/`를 실행하여 `routing.md` 파일이 존재하는지 확인한다.
- **이미 존재한다면:** `router` 에이전트 스폰을 Skip한다.
- **존재하지 않는다면:** 아래 명령어로 `router` 에이전트를 스폰한다.
   ```
   Agent(subagent_type="router", description="이슈 라우팅", prompt="ISSUE_NUMBER: [ISSUE_NUMBER]")
   ```
- **[중요 — 브랜치 이동]** (스폰 여부와 무관하게 무조건 실행) `cat .workspace/routing.md`를 실행하여 `BRANCH:` 값을 읽은 뒤, `git checkout {BRANCH 값}`을 실행하여 작업 브랜치로 안전하게 진입한다.
- **[CONFIDENCE 확인]** routing.md에 `CONFIDENCE: LOW`가 있으면 사용자에게 라우팅 결과를 보여주고 계속 진행할지 확인을 받는다. 확인 없이 Step 2로 진행하지 않는다.

---

### Step 2 — 코드 분석 (Agent: code-analyzer)

- [체크포인트] `ls .workspace/`를 실행하여 `scope.md` 파일이 존재하는지 확인한다.
- 이미 존재한다면 이 단계를 Skip하고 Step 3으로 즉시 넘어간다.
- 존재하지 않는다면 아래 명령어로 `code-analyzer` 에이전트를 스폰한다.

```
Agent(subagent_type="code-analyzer", description="코드베이스 분석", prompt="ROUTING_MD: .workspace/routing.md")
```

---

### Step 3 — 규칙 적용 (Agent: rule-translator)

- [체크포인트] `ls .workspace/`를 실행하여 `rules-applied.md` 파일이 존재하는지 확인한다.
- 이미 존재한다면 이 단계를 Skip하고 Step 4로 즉시 넘어간다.
- 존재하지 않는다면 아래 명령어로 `rule-translator` 에이전트를 스폰한다.

```
Agent(subagent_type="rule-translator", description="규칙 번역", prompt="ROUTING_MD: .workspace/routing.md\nSCOPE_MD: .workspace/scope.md")
```

---

### Step 4 — plan.md 컴파일 (Main Process)

- 이 단계에서는 새로운 코드를 탐색하거나 판단하지 않는다. 순수 템플릿 매핑만 수행한다.

1. `read_file .claude/templates/plan_template.md`를 실행하여 템플릿 뼈대를 확인하라.
2. `.workspace/`에 있는 `routing.md`, `scope.md`, `rules-applied.md` 3개의 파일을 읽어라.
3. `plan_template.md` 내부의 `{ }` 기호로 묶인 플레이스홀더(Placeholder) 영역을 찾고, 앞서 읽은 3개 파일의 실제 데이터로 **정확히 매핑하여 치환(Replace)하라.**
4. **절대 파일들을 단순히 이어붙이지(Concatenate) 마라.** 반드시 템플릿의 목차와 형식을 유지한 상태로 빈칸만 채워야 한다.
5. 완성된 결과를 `.workspace/plan.md`에 덮어쓰기로 저장하라.

---

### 5. 종료

1. MEMORY.md `# currentWork` 섹션을 작성한다:
   - `routing.md`의 `BRANCH` 값과 `ISSUE_SUMMARY` 값을 그대로 가져와 아래 포맷으로 작성.
    ```
    - 브랜치: `{BRANCH 값}`
    - 이슈: `#{ISSUE_NUMBER} {ISSUE_SUMMARY 값}`
    - 상태: plan.md 작성 완료, 승인 대기
    ```
2. 완료 신호를 출력하고 종료한다:
    ```
    PLAN_READY: {routing.md의 BRANCH 값}
    ```
    Worker가 이 신호를 감지하여 Slack 전송 및 승인 대기로 전환한다.

Worker 없이 터미널에서 직접 진행하려면:
```
/dev:review-plan
```
</execution_rules>