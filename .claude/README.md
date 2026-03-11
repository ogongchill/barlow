# .claude/ 디렉토리 가이드

Claude Code 설정 파일. 커맨드·에이전트·스킬·템플릿으로 구성된다.

---

## 구조

```
.claude/
├── commands/
│   ├── dev/             — 일상 개발 파이프라인 (/dev:*)
│   ├── arch/            — 아키텍처 파이프라인 (/arch:*)
│   └── harness-check.md — 검증 (두 파이프라인 공통)
├── agents/
│   ├── code-analyzer.md — 코드 분석 (dev 파이프라인 start-task에서 스폰)
│   └── test/            — 테스트 작성 (dev 파이프라인 continue-task에서 스폰)
├── skills/
│   ├── coding-rules/    — 레이어별 코딩 규칙
│   └── workflow-guide/  — 워크플로우 3단계 가이드
├── templates/           — ADR·보고서 작성용 템플릿
└── workspace/           — 작업 중 임시 파일 (plan.md 등, gitignore)
```

---

## 커맨드 흐름

### 일상 개발 (`/dev:*`)

```
/dev:start-task {N}          → 이슈 분석 + code-analyzer 스폰 + plan.md 작성  →  PLAN_READY
/dev:review-plan             → 터미널 Q&A + 확정                               →  PLAN_CONFIRMED
/dev:revise-plan "{피드백}"  → Slack 피드백 반영 (Worker 호출)                 →  PLAN_UPDATED | PLAN_UNCHANGED
/dev:continue-task {N}       → 코드 구현 + 테스트 + 커밋 + PR                  →  PR_CREATED
```

### 아키텍처 파이프라인 (`/arch:*`)

```
/arch:redesign   → 7-Lens 비판 + TO-BE 도출 + ADR 저장 (docs/adr/)
/arch:design     → 신규 시스템 초기 설계 + ADR 저장
/arch:plan [ADR] → 코드베이스 분해 + 구현 계획 확정 + 저장 (docs/plans/)
```

### 검증

```
/harness-check   → 아키텍처 규칙 체크리스트 (두 파이프라인 완료 전 필수)
```

---

## 에이전트

| 에이전트 | 스폰 시점 | 역할 |
|---|---|---|
| `code-analyzer` | `/dev:start-task` Step 5 | 코드베이스 탐색 → 4섹션 분석 반환 |
| `test-app-api` | `/dev:continue-task` — app:api 변경 시 | 인수 테스트 작성 |
| `test-app-batch` | `/dev:continue-task` — app:batch 변경 시 | 배치 잡 인수 테스트 작성 |
| `test-core-domain` | `/dev:continue-task` — core:domain 변경 시 | 도메인 단위 테스트 작성 |
| `test-core-service` | `/dev:continue-task` — 순수 로직 변경 시 | 서비스 단위 테스트 작성 |
| `test-app-api-docs` | `/dev:continue-task` — 신규 엔드포인트 시 | REST Docs 문서화 테스트 작성 |

---

## 스킬 로드 (CLAUDE.md §0 참조)

| 스킬 | 로드 파일 | 시점 |
|---|---|---|
| `workflow-guide` | `WORKFLOW-IMPL.md`, `WORKFLOW-VERIFY.md`, `WORKFLOW-SHIP.md` | 모든 개발 작업 시작 전 |
| `coding-rules` | 해당 레이어 규칙 파일 선택 로드 | 코드 작업 시 |