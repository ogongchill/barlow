# workflow-guide

## 트리거
모든 개발 작업(이슈 처리, 구현, 리뷰) 시작 시 이 스킬을 로드한다.

---

## 로드 방법

각 파일은 실제 사용 시점에 로드한다. `/dev:start-task` 단계에서는 로드하지 않는다.

| 파일 | 로드 시점 |
|---|---|
| `WORKFLOW-IMPL.md` | `/dev:continue-task` Step 2 (코드 구현) 진입 시 |
| `WORKFLOW-VERIFY.md` | `/dev:continue-task` Step 3 (테스트·검증) 진입 시 |
| `WORKFLOW-SHIP.md` | `/dev:continue-task` Step 4 (커밋·PR) 진입 시 |

---

## 전체 흐름 요약

```
이슈 접수
  → [/dev:start-task]    이슈 분석 + 브랜치 생성 + code-analyzer + plan.md 작성
  → [/dev:review-plan]   터미널 Q&A + 승인 (또는 Worker 경유 Slack 승인)
  → [/dev:continue-task] 코드 구현 → 테스트 → 커밋 & PR
```

> Phase 1~2 (분석 + plan.md + 승인)는 `/dev:start-task` 커맨드가 담당한다.
