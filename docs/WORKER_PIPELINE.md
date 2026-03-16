# Worker 자율 개발 파이프라인 — 설계 및 동작 상세

> `dev:start-task` 호출 시점부터 PR 생성까지 전체 흐름을 설계 의도와 함께 기술한다.
> 대상 독자: 이 시스템을 유지·개선하거나 새로운 프로젝트에 적용하려는 개발자.

---

## 목차

1. [시스템 구성 개요](#1-시스템-구성-개요)
2. [신호 기반 통신 프로토콜](#2-신호-기반-통신-프로토콜)
3. [전체 흐름 조감도](#3-전체-흐름-조감도)
4. [Phase 1 — dev:start-task](#4-phase-1--devstart-task)
   - 4-1. 서브에이전트 체인
   - 4-2. router 에이전트
   - 4-3. code-analyzer 에이전트
   - 4-4. rule-translator 에이전트
   - 4-5. plan.md 컴파일
5. [Phase 2 — plan.md 검토 루프](#5-phase-2--planmd-검토-루프)
   - 5-1. 터미널 직접 검토 경로 (dev:review-plan)
   - 5-2. Worker 중개 검토 경로 (dev:revise-plan)
6. [Phase 3 — dev:continue-task](#6-phase-3--devcontinue-task)
7. [.workspace/ 디렉토리의 역할](#7-workspace-디렉토리의-역할)
8. [체크포인트 패턴 — 멱등성 보장](#8-체크포인트-패턴--멱등성-보장)
9. [설계 의도 및 트레이드오프](#9-설계-의도-및-트레이드오프)
10. [새 프로젝트 적용 가이드](#10-새-프로젝트-적용-가이드)

---

## 1. 시스템 구성 개요

이 파이프라인은 세 가지 구성 요소의 협업으로 작동한다.

```
┌─────────────────────────────────────────────────────────────┐
│  Worker (외부 오케스트레이터)                                    │
│  - Claude Code CLI를 subprocess로 실행                        │
│  - stdout에서 신호(PLAN_READY:, PR_CREATED: 등)를 파싱          │
│  - Slack API로 plan.md 내용 전송 및 승인 대기                    │
│  - Slack 이벤트를 수신하여 다음 커맨드 트리거                        │
└──────────────────────┬──────────────────────────────────────┘
                       │  CLI 호출 / stdout 수신
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  Claude Code (Main Process)                                 │
│  - /dev:start-task, /dev:revise-plan, /dev:continue-task    │
│  - 각 커맨드(.md 파일)를 Skill 툴로 실행                          │
│  - 서브에이전트 스폰 및 결과 조합                                  │
│  - .workspace/ 파일 읽기·쓰기로 단계 간 상태 전달                   │
└──────────────────────┬──────────────────────────────────────┘
                       │  Agent 툴로 스폰
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  Sub-Agents (독립 프로세스)                                    │
│  - router: GitHub 이슈 → routing.md                          │
│  - code-analyzer: codebase 탐색 → scope.md                   │
│  - rule-translator: 규칙 특화 → rules-applied.md              │
│  - test-*: 테스트 코드 작성 (continue-task 중 병렬 실행)           │
└─────────────────────────────────────────────────────────────┘
```

### Worker의 정체

Worker는 Claude Code와 별개로 실행되는 외부 프로세스다. Claude Code를 `subprocess`로 실행하고 `stdout`에서 미리 정의된 신호 문자열을 파싱하여 다음 동작을 결정한다. Slack을 사용자 인터페이스로 삼아 비동기 승인 루프를 구현한다.

> Worker 구현체 참조: https://github.com/ogongchill/BE-claude-dev-worker

**Worker가 존재하는 이유**: Claude Code는 단일 커맨드 실행 후 종료된다. plan.md 작성 → 사람 검토 → 구현 시작이라는 인터럽트가 포함된 워크플로우를 자동화하려면, Claude Code 밖에서 상태를 관리하고 다음 커맨드를 결정하는 오케스트레이터가 필요하다.

---

## 2. 신호 기반 통신 프로토콜

Worker와 Claude Code의 통신은 stdout의 마지막 줄에 출력되는 신호 문자열로 이루어진다.

```
신호                          방향            의미
──────────────────────────────────────────────────────────────
PLAN_READY: {branch}          Claude → Worker  plan.md 완성, 검토 요청
PLAN_UNCHANGED                Claude → Worker  질문만 답변, plan.md 변경 없음
PLAN_UPDATED                  Claude → Worker  피드백 반영하여 plan.md 수정
PLAN_CONFIRMED: {branch}      Claude → Worker  plan.md 확정, 구현 시작 가능
PR_CREATED: {pr_url}          Claude → Worker  PR 생성 완료, 파이프라인 종료
ANSWER: {내용}                 Claude → Worker  Slack 스레드에 포스팅할 답변
```

**설계 의도**: 신호를 stdout 마지막 줄에 배치하면 Worker는 LLM 응답 전체를 파싱할 필요 없이 `last_line.startswith("PLAN_READY:")` 같은 단순 문자열 검사만으로 상태를 결정할 수 있다. LLM 응답 형식 변화에 대한 내성이 생긴다.

---

## 3. 전체 흐름 조감도

```
Worker
  │
  ├─ [1] claude "dev:start-task {issue}" 실행
  │         │
  │         ├─ router Agent      → .workspace/routing.md
  │         ├─ code-analyzer     → .workspace/scope.md
  │         ├─ rule-translator   → .workspace/rules-applied.md
  │         └─ plan 컴파일        → .workspace/plan.md
  │                   │
  │         stdout: "PLAN_READY: feat/issue/123"
  │
  ├─ [2] Slack에 plan.md 전송
  │
  ├─ [3] Slack 응답 대기 루프 ────────────────────────────────┐
  │         │                                              │
  │         │ 피드백 메시지 수신                                │
  │         ├─ claude "dev:revise-plan '{피드백}'" 실행       │
  │         │         │                                    │
  │         │         ├─ PLAN_UNCHANGED → Slack 답변만       │
  │         │         ├─ PLAN_UPDATED   → Slack에 수정 내용   │
  │         │         └─ PLAN_CONFIRMED → 루프 탈출 ─────────┘
  │
  ├─ [4] claude "dev:continue-task {issue}" 실행
  │         │
  │         ├─ 코드 구현 (plan.md 순서대로)
  │         ├─ 테스트 에이전트 병렬 실행
  │         ├─ 검증 (spotlessApply → unitTest → contextTest → harness-check)
  │         └─ 커밋 & PR 생성
  │                   │
  │         stdout: "PR_CREATED: https://github.com/..."
  │
  └─ [5] Slack에 PR URL 완료 알림 전송, 상태 초기화
```

---

## 4. Phase 1 — dev:start-task

**진입**: Worker가 `claude "/dev:start-task {issue_number}"` 실행
**종료 신호**: `PLAN_READY: {branch}`

### 4-1. 서브에이전트 체인

`dev:start-task`는 세 개의 서브에이전트를 **순차 직렬**로 실행한다. 각 에이전트의 출력이 다음 에이전트의 입력이 되기 때문이다.

```
router
  → routing.md (BC, 브랜치, 이슈 요약)
      ↓
code-analyzer (routing.md 참조)
  → scope.md (영향 파일, 핵심 구조, 리스크)
      ↓
rule-translator (routing.md + scope.md 참조)
  → rules-applied.md (구현 순서, 스펙, 검증 기준, 커밋 계획)
      ↓
Main Process: plan.md 컴파일
  → .workspace/plan.md
```

**왜 병렬이 아닌 직렬인가**: code-analyzer는 어떤 BC를 탐색할지 routing.md에 의존하고, rule-translator는 어떤 레이어를 다루는지 scope.md에 의존한다. 의존성 그래프가 선형이므로 병렬화가 불가능하다.

---

### 4-2. router 에이전트

**입력**: GitHub Issue 번호
**출력**: `.workspace/routing.md`

```
ISSUE_TITLE: "알림 센터 7일 이상 알림 자동 삭제 배치 Job 구현"
PRIMARY_TYPE: batch
SECONDARY_TYPE: NONE
BC: [NotificationCenter]
MODULE_HINTS: [app:batch, core:domain, infra:storage]
BRANCH_TYPE: batch
BRANCH: batch/issue/122
ISSUE_SUMMARY: "NotificationCenter BC의 7일 이상 된 알림을 Spring Batch로 정기 삭제한다."
```

**router의 결정 규칙**:
- `PRIMARY_TYPE` → `BRANCH_TYPE` 결정: `feat` / `fix` / `batch` / `refactor`
- `BC` → code-analyzer가 탐색할 도메인 범위 결정
- `MODULE_HINTS` → rule-translator가 선택할 규칙 파일 결정

---

### 4-3. code-analyzer 에이전트

**입력**: `.workspace/routing.md`
**출력**: `.workspace/scope.md`

scope.md는 세 섹션으로 구성된다:

```markdown
## 1. Target Scope & Entry Point
| 파일 경로 | 변경 유형 | 레이어 | 변경 의도 |

## 2. Core Structure Snippets
(주요 인터페이스·메서드 시그니처 발췌 — 구현부 제외)

## 3. Constraints & Risks
- 트랜잭션, 외부 인프라, BC 경계, 기존 패턴 충돌, 인접 scope 외, 기타

## 4. Reference Patterns
| 신규 파일 | 패턴 참조 파일 | 참조 이유 |
```

**설계 의도**: code-analyzer는 "어떤 파일을 어떻게 고쳐야 하는가"를 결정하지 않는다. "코드베이스에 지금 무엇이 있는가"만 기록한다. 설계 결정은 rule-translator가 규칙을 적용한 뒤 내린다. 탐색(fact-finding)과 설계(decision-making)를 분리하는 것이 목적이다.

---

### 4-4. rule-translator 에이전트

**입력**: `.workspace/routing.md` + `.workspace/scope.md`
**출력**: `.workspace/rules-applied.md`

rule-translator는 `scope.md`의 타겟 레이어를 보고 `.claude/skills/coding-rules/` 하위의 규칙 파일을 선택해 읽는다.

```
레이어 포함 여부 → 로드하는 규칙 파일
──────────────────────────────────────────────────────────────
core:domain 포함  → ARCHITECTURE.md + DOMAIN_RULES.md
core:service 포함 → ARCHITECTURE.md + SERVICE_RULES.md
app:api 포함      → SERVICE_RULES.md + REST_API_RULES.md
app:batch 포함    → BATCH_RULES.md
예외 추가 필요    → ERROR_HANDLING.md
```

rules-applied.md는 범용 규칙을 이슈 특화 스펙으로 번역한 결과물이다:

```markdown
## 구현 순서
1. `{실제 파일경로}` (신규) — {구체적 변경 의도}
2. ...

## 구현 스펙
### {실제 클래스명}
{주입 필드, 메서드 시그니처, 로직 흐름}

## 검증 기준
- [ ] {이 이슈에서 확인해야 하는 구체적 클래스명/메서드명}

## 테스트 계획
에이전트: test-core-domain, test-app-batch
시나리오: ...

## 커밋 계획
1. `batch : ...` — {해당 파일 목록}
```

**설계 의도**: 범용 규칙(`DOMAIN_RULES.md`)을 이슈마다 새로 해석하는 비용을 제거한다. rule-translator가 한 번 적용하면, 이후 continue-task는 rules-applied.md를 읽는 것만으로 무엇을 구현해야 하는지 파악한다.

---

### 4-5. plan.md 컴파일

세 에이전트가 완료된 후, Main Process가 `.claude/templates/plan_template.md`의 플레이스홀더를 세 파일의 실제 데이터로 치환하여 `.workspace/plan.md`를 생성한다.

```
plan_template.md의 섹션 → 데이터 소스
──────────────────────────────────────────────────────────────
## 이슈 요약        ← routing.md의 ISSUE_SUMMARY
## 변경 범위        ← scope.md Section 1
## 구현 순서        ← rules-applied.md ## 구현 순서
## 구현 스펙        ← rules-applied.md ## 구현 스펙
## 검증 기준        ← rules-applied.md ## 검증 기준
## 테스트 계획      ← rules-applied.md ## 테스트 계획
## 커밋 계획        ← rules-applied.md ## 커밋 계획
## 리스크 & 제약    ← scope.md Section 3
## 구현 범위 외     ← scope.md Section 3 인접 scope 외
```

**왜 별도 컴파일 단계가 존재하는가**: 세 에이전트의 출력을 단순 연결(concatenate)하면 구조가 무너진다. 템플릿을 고정해두고 데이터를 끼워 넣으면, 어떤 이슈든 동일한 형식의 plan.md가 생성된다. continue-task는 항상 동일한 위치에서 필요한 정보를 찾을 수 있다.

---

## 5. Phase 2 — plan.md 검토 루프

plan.md가 완성되면 사람이 검토하고 확정해야 한다. 두 가지 경로가 존재한다.

### 5-1. 터미널 직접 검토 경로 (dev:review-plan)

Worker 없이 터미널에서 개발자가 직접 Claude와 상호작용한다.

```
개발자
  │
  ├─ /dev:review-plan 실행
  │     → plan.md 핵심 내용 요약 출력
  │
  ├─ 질문/수정 요청 입력 → Claude가 답변 또는 plan.md 수정
  │
  └─ "확정" 또는 "승인" 입력
        → MEMORY.md # currentWork 섹션 상태 업데이트
        → stdout: "PLAN_CONFIRMED: {branch}"
        → 개발자가 /dev:continue-task {issue} 실행
```

### 5-2. Worker 중개 검토 경로 (dev:revise-plan)

```
Worker
  │
  ├─ PLAN_READY: 신호 수신 후 plan.md 내용을 Slack 채널에 전송
  │
  └─ Slack 응답 수신 시 분기:
        │
        ├─ 텍스트 피드백 → claude "dev:revise-plan '{피드백}'" 실행
        │       │
        │       ├─ PLAN_UNCHANGED → "ANSWER:" 파싱 → Slack 스레드에 답변 포스팅
        │       ├─ PLAN_UPDATED   → "ANSWER:" 파싱 → 수정 내용 + plan.md 업데이트 포스팅
        │       └─ PLAN_CONFIRMED → 루프 탈출, continue-task 트리거
        │
        └─ 승인 버튼/키워드 → claude "dev:continue-task {issue}" 실행
```

**dev:revise-plan의 피드백 분류 로직**:

```
피드백 유형        출력 신호                      plan.md 변경
────────────────────────────────────────────────────────────
질문              ANSWER: + PLAN_UNCHANGED    없음
수정 요청          ANSWER: + PLAN_UPDATED      수정 후 덮어쓰기
수정 + 확정 동시    ANSWER: + PLAN_UPDATED      수정 후 덮어쓰기
                  + PLAN_CONFIRMED           루프 탈출
```

---

## 6. Phase 3 — dev:continue-task

**진입**: Worker가 `PLAN_CONFIRMED:` 신호 수신 후 `claude "/dev:continue-task {issue_number}"` 실행
**종료 신호**: `PR_CREATED: {pr_url}`

### 6-1. 사전 확인

```bash
# plan.md 존재 여부 확인
cat .workspace/plan.md

# 브랜치 형식 확인 — {feat|fix|batch|refactor}/issue/{번호} 형식이 아니면 중단
git branch --show-current
```

브랜치 확인은 "엉뚱한 브랜치에서 코드를 쓰는" 사고를 방지하는 안전장치다. start-task 단계에서 routing.md의 BRANCH 값으로 브랜치를 생성하고 이동했으므로, continue-task 진입 시 이미 올바른 브랜치여야 한다.

### 6-2. 코드 구현

plan.md의 `## 구현 순서`에 명시된 파일을 순서대로 작성한다. 구현의 실제 내용은 `## 구현 스펙`의 pseudo-code를 Java 코드로 변환한 것이다.

**의존성 방향 보장**: 구현 순서는 rule-translator가 아키텍처 규칙에 따라 결정했으므로 항상 `core:domain → core:service → infra:storage → app:api` 순서가 된다. 역방향 의존성이 생길 수 없다.

**이탈 조건**: 구현 중 plan.md에 명시되지 않은 BC 경계·API 계약 변경이 불가피하면 **즉시 중단하고 사용자에게 확인**한다. plan.md 외 범위 변경은 계획 없이 진행된 설계 변경이므로 새 plan.md 주기가 필요하다.

### 6-3. 테스트 에이전트 병렬 실행

plan.md의 `## 테스트 계획` 섹션에 명시된 에이전트를 병렬로 스폰한다.

```
[항상]   test-core-domain   ─┐
[항상]   test-app-api        ├─ 동시 실행 (모듈이 독립적)
[해당 시] test-core-service ─┘
[선택]   test-app-batch         ← batch 레이어 변경 시
[선택]   test-app-api-docs      ← REST API 엔드포인트 변경 시
[선택]   test-infra-*           ← infra 레이어 변경 시
```

각 에이전트는 `PLAN_MD: .workspace/plan.md`를 입력으로 받는다. 에이전트는 plan.md의 `## 테스트 계획`을 읽어 시나리오를 파악하고, `## 구현 스펙`을 읽어 클래스명·메서드 시그니처를 확인한 뒤 테스트 코드를 작성한다.

### 6-4. 검증

```bash
./gradlew spotlessApply      # 포맷 적용 (항상 먼저)
./gradlew unitTest           # 도메인 단위 테스트
./gradlew contextTest        # DB 포함 인수 테스트
./gradlew restDocsTest       # REST Docs 생성 테스트 (API 변경 시)
/harness-check               # 아키텍처 규칙 최종 관문
```

**harness-check PASS 없이 커밋 금지.** harness-check는 5개 영역을 체크한다:
- Architecture: 레이어 의존성 방향 (core:domain에 @Entity 금지 등)
- Domain: 불변 규칙 (setter 없음, Spring 어노테이션 없음)
- Service: 트랜잭션 규칙, infra:* 직접 주입 금지
- Error Handling: CoreDomainException 상속, static factory
- Testing: 테스트 태그 사용 여부

### 6-5. 커밋 & PR 생성

plan.md의 `## 커밋 계획`에 명시된 순서대로 레이어별 커밋을 분리한다.

```bash
# 예시 — batch 이슈
git commit -m "batch : NotificationCenter 배치 전용 Repository 신규 추가"
git commit -m "batch : 알림 센터 7일 이상 알림 삭제 배치 Job 구현"
git commit -m "test : NotificationCenter 클린업 배치 인수 테스트 추가"
git commit -m "lint : lint 적용"
```

PR 생성 시 `routing.md`의 `BRANCH_TYPE:` 값을 제목 prefix로 사용한다.

```bash
gh pr create \
  --base develop \
  --title "{BRANCH_TYPE}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

### 6-6. 상태 정리 및 종료

```bash
# 1. workspace 초기화 (재사용 방지)
rm -f .workspace/routing.md .workspace/scope.md \
      .workspace/rules-applied.md .workspace/plan.md

# 2. MEMORY.md # currentWork 섹션 전체 삭제

# 3. 종료 신호 출력
PR_CREATED: https://github.com/org/repo/pull/456
```

Worker는 이 신호를 수신하여 Slack에 완료 알림을 보내고 상태를 초기화한다.

---

## 7. .workspace/ 디렉토리의 역할

`.workspace/`는 에이전트 간·단계 간 상태를 파일로 전달하는 임시 저장소다.

```
.workspace/
  ├── routing.md       ← router 산출물  → code-analyzer, rule-translator, Main Process 입력
  ├── scope.md         ← code-analyzer 산출물  → rule-translator 입력
  ├── rules-applied.md ← rule-translator 산출물 → plan 컴파일 입력
  └── plan.md          ← Main Process 산출물  → review/revise/continue 전체 진행 동안 사용
```

**파일 기반 통신의 이유**: 에이전트는 독립 프로세스로 실행되므로 메모리를 공유할 수 없다. 파일은 에이전트 간 계약(contract)이다. 파일이 존재하면 해당 단계가 완료된 것이고, 없으면 아직 실행되지 않은 것이다.

**plan.md의 수명주기**:
```
start-task: .workspace/plan.md 최초 생성
revise-plan: 피드백 반영 시 덮어쓰기
review-plan: 사용자 요청 시 덮어쓰기
continue-task: 읽기 전용 (구현 지침서로 사용)
continue-task 완료: rm -f로 삭제 (다음 이슈 오염 방지)
```

---

## 8. 체크포인트 패턴 — 멱등성 보장

각 단계 진입 시 `.workspace/`에 해당 파일이 이미 있으면 스킵한다. 이 패턴 덕분에 파이프라인이 중간에 실패하거나 재시작되어도 완료된 단계를 반복하지 않는다.

```
start-task 실행 시:
  .workspace/routing.md    있음? → router 스킵
  .workspace/scope.md      있음? → code-analyzer 스킵
  .workspace/rules-applied.md 있음? → rule-translator 스킵
```

**예외**: plan.md는 체크포인트 검사 없이 항상 재컴파일한다. routing.md/scope.md/rules-applied.md 중 하나라도 새로 생성됐다면 plan.md도 갱신되어야 하기 때문이다.

**브랜치 이동은 항상 실행**: 체크포인트로 router를 스킵하더라도 `git checkout {BRANCH}` 는 반드시 실행한다. start-task가 브랜치 이동 없이 종료될 경우 후속 단계가 엉뚱한 브랜치에서 실행된다.

---

## 9. 설계 의도 및 트레이드오프

### 9-1. 왜 단일 에이전트가 아닌 체인인가

단일 에이전트가 "이슈 분석부터 plan.md 작성까지" 모두 수행하면:
- 탐색(fact-finding)과 설계(decision-making)가 뒤섞임
- 에이전트 한 번 실패 시 전 단계 재실행 필요
- 각 단계 결과를 독립적으로 검토하거나 수정 불가능

체인 구조에서는 각 에이전트가 단일 책임을 가지며, 중간 파일(routing.md, scope.md 등)이 각 에이전트의 계약이 된다. 특정 단계만 재실행하거나 출력 파일을 직접 수정하는 것이 가능하다.

### 9-2. 왜 plan.md 승인 루프가 존재하는가

"Claude가 알아서 구현하면 되지 않는가"라는 질문에 대한 답변이다.

BC 경계 변경, API 계약 변경, 설계 방향 결정은 구현 후 수정하면 비용이 크다. plan.md 단계에서 사람이 개입하면 "틀린 방향으로 구현 완료 → 전면 재작업"을 방지한다. 계획을 검토하는 비용(몇 분)이 재작업 비용(몇 시간)보다 훨씬 작다.

### 9-3. 왜 신호를 stdout 마지막 줄에 배치하는가

LLM의 응답 형식은 자연어이므로 구조화 파싱이 어렵다. 신호를 마지막 줄에 고정하면:
- Worker는 응답 내용 전체를 파싱하지 않아도 된다
- 신호가 LLM 답변 중간에 섞여 오탐되는 문제가 없다
- 커맨드별 신호 종류가 명확히 정의되어 있으므로 예상치 못한 신호는 Worker가 무시하거나 경고를 낼 수 있다

### 9-4. Slack vs 터미널 직접 검토

두 경로를 병존시킨 이유:
- **Slack 경로**: 자리를 비운 상태에서도 모바일로 plan.md를 검토하고 승인할 수 있다. 비동기 워크플로우.
- **터미널 경로**: 즉각 피드백이 필요하거나 Worker 없이 로컬에서 빠르게 테스트할 때 사용.

두 경로 모두 `PLAN_CONFIRMED:` 신호로 수렴하므로 continue-task는 진입 경로를 구분하지 않아도 된다.

### 9-5. .workspace 파일 vs MEMORY.md

| 항목 | .workspace/*.md | MEMORY.md |
|---|---|---|
| 용도 | 단계 간 구조화 데이터 전달 | 세션 간 상태 요약 (사람이 읽는 형태) |
| 수명 | 파이프라인 완료 시 삭제 | 영구 보존 (다음 세션에서 참조) |
| 내용 | 파일 경로, 클래스명, 시그니처 등 기계 처리용 | 현재 작업 요약, 브랜치명, 진행 상태 |
| 크기 | 제한 없음 | 200줄 상한 (MEMORY.md 자동 주입 제약) |

MEMORY.md의 `# currentWork` 섹션은 세션 격리(/clear) 후 새 세션에서 "어디까지 왔는가"를 즉시 파악하기 위한 인간 친화적 인덱스다.

---

## 10. 새 프로젝트 적용 가이드

이 파이프라인을 다른 프로젝트에 이식할 때 변경해야 하는 부분과 유지해야 하는 부분을 구분한다.

### 반드시 교체해야 하는 부분 (프로젝트 특화)

| 파일 | 교체 내용 |
|---|---|
| `.claude/skills/coding-rules/*.md` | 프로젝트 아키텍처 규칙 (레이어 구조, 패키지 명명 등) |
| `docs/DOMAIN_ENCYCLOPEDIA.md` | 프로젝트 도메인 용어 및 BC 목록 |
| `.claude/templates/plan_template.md` | plan.md 형식 (프로젝트 표준에 맞게) |
| `.claude/templates/routing_template.md` | 브랜치 명명 규칙 |
| `CLAUDE.md` | 커밋 컨벤션, 테스트 태그 명령어 |
| `.github/PULL_REQUEST_TEMPLATE.md` | PR 본문 템플릿 |

### 그대로 재사용 가능한 부분 (범용 메커니즘)

| 파일 | 재사용 이유 |
|---|---|
| `.claude/commands/dev/start-task.md` | 에이전트 체인 + 체크포인트 패턴은 범용 |
| `.claude/commands/dev/continue-task.md` | plan.md 기반 구현 + 검증 흐름은 범용 |
| `.claude/commands/dev/revise-plan.md` | 피드백 분류 + 신호 출력 로직은 범용 |
| `.claude/commands/dev/review-plan.md` | 터미널 직접 검토 인터페이스는 범용 |
| `.claude/templates/scope_template.md` | 4섹션 구조는 대부분 프로젝트에 적합 |
| Worker 통신 프로토콜 | 신호 기반 stdout 파싱은 언어/스택 무관 |

### 선택적 조정 항목

- **Worker 구현 언어**: Python/Node.js/Go 등 어느 언어로도 구현 가능. stdout 마지막 줄에서 신호를 읽는 단순 로직.
- **알림 채널**: Slack 대신 Discord, Teams, 이메일 등으로 교체 가능. plan.md를 Markdown으로 전송할 수 있는 채널이면 된다.
- **승인 방식**: 버튼 클릭, 특정 단어 입력, 이모지 반응 등 어느 방식이든 Worker가 `PLAN_CONFIRMED:` 신호를 발생시키기만 하면 된다.
- **테스트 에이전트 목록**: 프로젝트의 테스트 레이어 구조에 맞게 agents/*.md 파일을 추가/교체한다.

---

## 부록: 파일 참조 맵

```
파이프라인 단계         참조하는 파일
──────────────────────────────────────────────────────────────────
start-task 진입        .claude/commands/dev/start-task.md
  router               docs/DOMAIN_ENCYCLOPEDIA.md (BC 매핑)
                       .claude/templates/routing_template.md
  code-analyzer        .workspace/routing.md
                       .claude/templates/scope_template.md
  rule-translator      .workspace/routing.md
                       .workspace/scope.md
                       .claude/skills/coding-rules/*.md (레이어 선택)
                       .claude/templates/rules_template.md
  plan 컴파일           .workspace/{routing,scope,rules-applied}.md
                       .claude/templates/plan_template.md
start-task 완료        .workspace/plan.md (생성)
                       memory/MEMORY.md (# currentWork 기록)

review-plan            .claude/commands/dev/review-plan.md
                       .workspace/plan.md

revise-plan            .claude/commands/dev/revise-plan.md
                       .workspace/plan.md
                       .workspace/routing.md (BRANCH 값)

continue-task 진입     .claude/commands/dev/continue-task.md
                       .workspace/plan.md
  코드 구현             .workspace/plan.md (## 구현 순서 + ## 구현 스펙)
  테스트 에이전트        .workspace/plan.md (## 테스트 계획)
  PR 생성              .workspace/routing.md (BRANCH_TYPE)
                       .github/PULL_REQUEST_TEMPLATE.md
continue-task 완료     .workspace/*.md (삭제)
                       memory/MEMORY.md (# currentWork 삭제)
```
