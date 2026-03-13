---
description: 터미널에서 plan.md를 검토하고 Claude와 직접 Q&A 후 확정한다
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
- 설계 결정 요약
- 커밋 계획

### 3. 대화형 Q&A

사용자 입력을 기다린다. 입력 유형별 처리:

**질문인 경우** ("왜 ~", "~가 맞나요?" 등):
- 설계 근거를 설명한다
- plan.md 수정이 필요하면 수정 후 변경 내용을 알린다

**수정 요청인 경우** ("~을 바꿔줘", "~를 추가해줘" 등):
- plan.md를 수정한다
- 수정된 항목을 명시적으로 알린다

**"확정" 입력인 경우**:
- 아래 종료 순서를 실행한다

### 4. 확정 처리

사용자가 "확정" 또는 "승인"을 입력하면:

1. plan.md 최종 버전을 저장한다
2. MEMORY.md `# currentWork` 섹션을 업데이트한다:
   ```
   - [x] 분석 + plan.md 작성
   - [x] 승인
   - [ ] 코드 구현
   ...
   ```
3. 마지막 줄에 아래를 출력하고 종료한다:
   ```
   PLAN_CONFIRMED: {type}/issue/{issue_number}
   ```

이 커맨드는 Worker 없이 터미널에서 직접 진행할 때 사용한다.
확정 후 바로 다음 단계로 진행하려면:
```
→ /dev:continue-task {issue_number}
```