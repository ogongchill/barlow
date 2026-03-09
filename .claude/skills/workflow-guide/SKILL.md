# workflow-guide

## 트리거
모든 개발 작업(이슈 처리, 구현, 리뷰) 시작 시 이 스킬을 로드한다.
코딩 규칙이 필요한 경우 추가로 `coding-rules` 스킬을 로드한다.

---

## 제공 파일 — Phase별 지연 로드

**컨텍스트 효율을 위해 현재 수행 중인 Phase에 해당하는 파일만 로드한다.**

| 로드 시점 | 파일 | 내용 |
|---|---|---|
| 이슈 접수 시 | `WORKFLOW-TRIAGE.md` | Tier 판정 기준 |
| Tier 판정 후 (계획 단계) | `WORKFLOW-PLAN.md` | Phase 0~3: 분석 & 계획 |
| 구현 시작 시 | `WORKFLOW-IMPL.md` | Phase 4: 코드 구현 체크리스트 |
| 테스트 시작 시 | `WORKFLOW-VERIFY.md` | Phase 5~6: 테스트 & 검증 |
| 커밋/PR 시 | `WORKFLOW-SHIP.md` | Phase 7~8: 커밋 & PR |
| 전체 구조 파악 필요 시 | `WORKFLOW.md` | 인덱스 (Phase 파일 맵) |

---

## 핵심 내용 요약

- **Phase -1**: Tier 판정 (Tier 1 자율 / Tier 2 계획 후 승인 / Tier 3 분석만)
- **Phase 0**: 스킬 & 규칙 로드 / DOMAIN_ENCYCLOPEDIA — 해당 BC 섹션만 지연 로드
- **Phase 1~3**: 요구사항 분석 → 코드베이스 탐색 → 구현 계획 (승인 대기)
- **Phase 4**: 코드 구현 (domain → service → infra → api 순서)
- **Phase 5~6**: 테스트 Agent 병렬 실행 → 린트 → Harness 검증
- **Phase 7~8**: 커밋 → 브랜치 & PR 생성

**세션 격리 권장 지점:**
- Phase 3 완료 & 승인 후 → `/clear` → "Phase 4 진행해" (복잡 작업만)
- Phase 6 완료 후 (테스트 전체 PASS + Harness PASS) → `/clear` → "Phase 7 진행해"