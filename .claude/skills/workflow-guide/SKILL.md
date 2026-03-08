# workflow-guide

## 트리거
모든 개발 작업(이슈 처리, 구현, 리뷰) 시작 시 이 스킬을 로드한다.
코딩 규칙이 필요한 경우 추가로 `coding-rules` 스킬을 로드한다.

---

## 제공 파일

```
WORKFLOW.md  — 요구사항 접수 → PR 생성까지의 자율형 개발 파이프라인 (Phase -1~8)
```

---

## 핵심 내용 요약

- **Phase -1**: Tier 판정 (Tier 1 자율 / Tier 2 계획 후 승인 / Tier 3 분석만)
- **Phase 0**: 스킬 & 규칙 로드 (`coding-rules` 스킬 추가 로드)
- **Phase 1~3**: 요구사항 분석 → 코드베이스 탐색 → 구현 계획
- **Phase 4~6**: 코드 구현 → 테스트 → 린트 & `/project:harness-check` 검증
- **Phase 7~8**: 커밋 → 브랜치 & PR

**Tier별 진입 커맨드:**

| 커맨드 | 동작 |
|---|---|
| `/project:tier:dev-auto` | Tier 1 강제 자율 진행 |
| `/project:tier:dev-plan` | Tier 2 계획 후 승인 강제 |
| `/project:tier:dev-analyze` | Tier 3 사전 스크리닝 |