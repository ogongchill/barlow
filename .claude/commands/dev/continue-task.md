---
description: plan.md 승인 후 코드 구현부터 PR 생성까지 진행한다. Worker가 호출한다.
model: sonnet
---

# /dev:continue-task

## 입력

`[ISSUE_NUMBER]`: GitHub Issue 번호 (필수)

입력이 없으면 즉시 중단한다:
```
사용법: /dev:continue-task {issue_number}
예시:   /dev:continue-task 120
```

---

## 실행 순서

### 1. 사전 확인

`.workspace/plan.md`를 읽는다.

파일이 없으면 즉시 중단한다:
```
[continue-task] plan.md가 없습니다. /dev:start-task {issue_number}를 먼저 실행하세요.
```

브랜치 확인:
```bash
git branch --show-current
```
`{feat|fix|batch|refactor}/issue/[ISSUE_NUMBER]` 형식이 아니면 중단한다.

### 2. 코드 구현

plan.md의 `## 구현 순서` 섹션에 명시된 번호 순서대로 파일을 구현한다.
각 파일은 `## 구현 스펙` 섹션의 pseudo-code를 실제 Java 코드로 변환한다.
각 파일 작성 완료 시 `## 검증 기준` 체크리스트의 해당 항목을 확인한다.

**구현 중 plan.md에 명시되지 않은 BC 경계·API 계약 변경이 불가피하다고 판단되면 코드 작성을 즉시 멈추고 사용자에게 확인한다.**

### 3. 테스트 & 검증

plan.md의 `## 테스트 계획` 섹션에 명시된 에이전트를 아래 형식으로 스폰하여 테스트를 작성한다:
```
Agent("{테스트 에이전트명}", prompt="PLAN_MD: .workspace/plan.md")
```

테스트 작성 완료 후 순서대로 실행한다:
```bash
./gradlew spotlessApply
./gradlew unitTest
./gradlew contextTest
./gradlew restDocsTest
/harness-check
```

테스트 FAIL 시 동일 명령 반복 재시도 금지. 반드시 원인을 파악하고 수정한 뒤 재검증한다. (Gradle 테스트 및 harness-check 모두 해당)

**harness-check PASS 없이 커밋 금지.**

### 4. 커밋 & PR

plan.md의 `## 커밋 계획` 섹션에 명시된 순서와 메시지로 커밋한다.

PR 생성 전 `.workspace/routing.md`에서 `BRANCH_TYPE:` 값을 읽어 PR 제목의 `{type}`으로 사용한다:
```bash
gh pr create \
  --base develop \
  --title "{BRANCH_TYPE}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

PR 제목: 70자 이하.

### 5. 상태 정리 및 종료 신호

PR 생성이 완료되면 아래 순서로 상태를 정리하고 종료한다:

1. `.workspace/` 디렉토리를 초기화한다: (반드시 신호 출력 전에 수행할 것)
   ```bash
   rm -f .workspace/routing.md .workspace/scope.md \
         .workspace/rules-applied.md .workspace/plan.md
   ```
2. `MEMORY.md` 파일을 열어 `# currentWork` 섹션을 전체 삭제한다.
3. 마지막 줄에 완료 신호를 출력하고 종료한다:
   ```
   PR_CREATED: {pr_url}
   ```

Worker가 이 신호를 감지하여 Slack에 완료 메시지를 전송하고 상태를 초기화한다.