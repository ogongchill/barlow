---
description: plan.md 승인 후 코드 구현부터 PR 생성까지 진행한다. Worker가 호출한다.
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
`{type}/issue/[ISSUE_NUMBER]`가 아니면 중단한다. (`{type}`은 `feat` / `fix` / `refactor` 등)

### 2. 코드 구현

`WORKFLOW-IMPL.md`를 읽고 Phase 3(코드 구현)을 진행한다.

구현 순서:
```
plan.md 구현 스펙의 섹션 순서대로 구현한다.
단, core:service는 Implement Layer(impl.*) → Business Layer(business.*) 순서로 구현한다.
```

각 레이어 완료 시 WORKFLOW-IMPL.md 체크리스트를 실행한다.

### 3. 테스트 & 검증

`WORKFLOW-VERIFY.md`를 읽고 Phase 4(테스트 & 검증)를 진행한다.

```bash
./gradlew spotlessApply
./gradlew unitTest
./gradlew contextTest
/harness-check
```

테스트 FAIL 시 동일 명령 반복 재시도 금지. 반드시 원인을 파악하고 수정한 뒤 재검증한다. (Gradle 테스트 및 harness-check 모두 해당)

### 4. 커밋 & PR

`WORKFLOW-SHIP.md`를 읽고 Phase 5(커밋 & PR)를 진행한다.

레이어별 커밋 분리 후 PR 생성:
```bash
gh pr create \
  --base develop \
  --title "{type}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

### 5. 상태 정리 및 종료 신호

PR 생성이 완료되면 아래 순서로 상태를 정리하고 종료한다:

1. `MEMORY.md` 파일을 열어 `# currentWork` 섹션을 전체 삭제한다. (반드시 신호 출력 전에 수행할 것)
2. 마지막 줄에 완료 신호를 출력하고 종료한다:
   ```
   PR_CREATED: {pr_url}
   ```

Worker가 이 신호를 감지하여 Slack에 완료 메시지를 전송하고 상태를 초기화한다.