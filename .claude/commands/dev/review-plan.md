---
description: 터미널에서 plan.md를 검토하고 Claude와 직접 Q&A 후 확정한다
model: opus
---

# /dev:review-plan

터미널 앞에 있을 때 사용한다.
plan.md를 읽고 사용자 질문에 답변하거나 수정한 뒤, "확정" 입력 시 Worker에게 신호를 전달한다.

---

## 실행 순서

### 1. 현재 plan.md 로드

`.workspace/plan.md`를 읽는다.

파일이 없으면 즉시 중단한다:
```
[review-plan] plan.md가 없습니다. /dev:start-task {issue_number}를 먼저 실행하세요.
```

### 2. plan.md 내용 요약 출력

현재 plan.md의 핵심 내용을 채팅에 출력한다:
- 변경 범위 목록
- 구현 순서
- 구현 스펙 요약
- 검증 기준
- 테스트 계획
- 커밋 계획

### 3. 대화형 Q&A

사용자 입력을 기다린다. 입력 유형별 처리:

**질문인 경우** ("왜 ~", "~가 맞나요?" 등):
- 설계 근거를 설명한다
- plan.md 수정이 필요하면 수정하고 저장한다 (`.workspace/plan.md` 덮어쓰기), 변경 내용을 알린다

**수정 요청인 경우** ("~을 바꿔줘", "~를 추가해줘" 등):
- plan.md를 수정하고 저장한다 (`.workspace/plan.md` 덮어쓰기)
- 수정된 항목을 명시적으로 알린다

**"확정" 입력인 경우**:
- 아래 종료 순서를 실행한다

### 4. 확정 처리

사용자가 "확정" 또는 "승인"을 입력하면:

1. plan.md 최종 버전을 저장한다
2. MEMORY.md `# currentWork` 섹션의 `상태:` 값을 업데이트한다:
   ```
   - 상태: 승인 완료, 구현 진행 중
   ```
3. `.workspace/routing.md`에서 `BRANCH:` 값을 읽어 아래 형식으로 출력하고 종료한다:
   ```
   PLAN_CONFIRMED: {routing.md의 BRANCH 값}
   ```

이 커맨드는 Worker 없이 터미널에서 직접 진행할 때 사용한다.
확정 후 바로 다음 단계로 진행하려면:
```
→ /dev:continue-task {issue_number}
```