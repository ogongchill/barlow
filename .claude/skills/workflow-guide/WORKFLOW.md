# 자율형 개발 파이프라인 (Workflow) — 인덱스

요구사항 접수부터 PR 생성까지의 전체 흐름을 정의한다.

> 파일이 Phase별로 분리되어 있다. **현재 진행 Phase에 맞는 파일만 로드**한다.
> 단계 명칭: 이 파이프라인은 **Phase N**으로 지칭한다.
> `/arch-redesign`, `/arch-design` 커맨드는 **Stage N**, `/arch-plan` 커맨드는 **Step N**을 사용한다.

---

## Phase 파일 맵

| Phase | 파일 | 내용 |
|---|---|---|
| **-1** | `WORKFLOW-TRIAGE.md` | Tier 판정 기준 (Tier 1/2/3 분류, 이슈 신호 탐지) |
| **0~3** | `WORKFLOW-PLAN.md` | 스킬 로드 → 요구사항 분석 → 코드베이스 탐색 → 구현 계획 |
| **4** | `WORKFLOW-IMPL.md` | 코드 구현 순서 & 체크리스트 |
| **5~6** | `WORKFLOW-VERIFY.md` | 테스트 작성 → 린트 & Harness 검증 |
| **7~8** | `WORKFLOW-SHIP.md` | 커밋 → 브랜치 & PR / 피드백 루프 / 금지사항 |

---

## 전체 흐름 요약

```
이슈 접수
  → Phase -1 : Tier 판정          (WORKFLOW-TRIAGE.md)
  → Phase 0~3: 분석 & 계획        (WORKFLOW-PLAN.md)
  ↓ [세션 격리 — 복잡 작업만: 구현계획.md 작성 완료 후 /clear]
  → Phase 4  : 코드 구현          (WORKFLOW-IMPL.md)
  → Phase 5~6: 테스트 & 검증      (WORKFLOW-VERIFY.md)
  ↓ [세션 격리 권장: Harness PASS 후 /clear]
  → Phase 7~8: 커밋 & PR          (WORKFLOW-SHIP.md)
```

> **세션 격리 조건**
> - Phase 3→4 사이: 구현계획.md + 탐색결과.md가 작성된 **복잡 작업**에만 적용
>   단순 작업(변경 파일 < 5개)은 /clear 없이 연속 진행
> - Phase 6→7 사이: 테스트 전체 PASS + Harness PASS 확인 후 적용

---

## Tier별 진입 커맨드

| 커맨드 | 동작 |
|---|---|
| `/project:tier:dev-auto` | Tier 1 강제 자율 진행 |
| `/project:tier:dev-plan` | Tier 2 계획 후 승인 강제 |
| `/project:tier:dev-analyze` | Tier 3 사전 스크리닝 |