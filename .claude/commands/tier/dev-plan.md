Tier 2 계획 후 승인 모드로 강제 실행한다.

현재 이슈 또는 요구사항을 Tier 2(계획 후 승인)로 처리한다.
평소의 Tier 판정을 건너뛰고 WORKFLOW.md Phase 0~3을 실행한 뒤 구현계획.md를 작성하고 반드시 사용자 승인을 기다린다.

다음을 채팅에 출력 후 진행한다:
```
[/project:dev-plan] Tier 2 계획 모드로 진행합니다.
이슈: {이슈 제목 또는 요구사항 요약}
구현계획.md 작성 후 승인을 기다립니다.
```

실행 순서:
1. WORKFLOW.md Phase 0 — 스킬 & 규칙 로드
2. WORKFLOW.md Phase 1 — 요구사항 분석
3. WORKFLOW.md Phase 2 — 코드베이스 탐색 (탐색결과.md 작성)
4. WORKFLOW.md Phase 3 — 구현계획.md 작성
5. 사용자 승인 대기 — "진행해" 입력 전까지 코드 작성 금지
6. 승인 후 Phase 4부터 계속