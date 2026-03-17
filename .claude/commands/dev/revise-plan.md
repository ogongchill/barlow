---
description: Slack 피드백을 plan.md에 반영하고 답변을 출력한다. Worker가 호출한다.
model: sonnet
---

# /dev:revise-plan

## 입력

`[FEEDBACK]`: Slack에서 받은 사용자 메시지 (필수)

입력이 없으면 즉시 중단한다:
```
사용법: /dev:revise-plan "{feedback}"
예시:   /dev:revise-plan "NotificationCenter 대신 다른 BC를 써야 할 것 같은데요"
```

---

## 실행 순서

### 1. 현재 plan.md 및 routing.md 로드

`.workspace/plan.md`와 `.workspace/routing.md`를 읽는다.

파일이 없으면 즉시 중단한다:
```
[revise-plan] plan.md가 없습니다. /dev:start-task {issue_number}를 먼저 실행하세요.
```

### 2. 피드백 분류

피드백 유형을 판단한다:

**질문** ("왜 ~", "~가 맞나요?", "~를 설명해줘" 등):
- plan.md 수정 없이 설계 근거만 설명한다
- 출력 형식:
  ```
  ANSWER: {답변 내용}
  PLAN_UNCHANGED
  ```

**수정 요청** ("~을 바꿔줘", "~를 빼줘", "~를 추가해줘" 등):
- plan.md를 수정하고 저장한다 (`.workspace/plan.md` 덮어쓰기)
- 출력 형식:
  ```
  ANSWER: {변경 내용 요약}
  PLAN_UPDATED
  ```

**수정 요청 + 확정 동시** ("~바꿔주세요. 그리고 OK" 등):
- plan.md를 수정하고 저장한다 (`.workspace/plan.md` 덮어쓰기)
- 출력 형식:
  ```
  ANSWER: {변경 내용 요약}
  PLAN_UPDATED
  PLAN_CONFIRMED: {routing.md의 BRANCH 값}
  ```

### 3. 종료

답변 출력 후 종료한다.

Worker가 `ANSWER:` 라인을 파싱하여 Slack 스레드에 포스팅한다.
Worker가 `PLAN_UPDATED`를 감지하면 업데이트된 plan.md 내용도 함께 포스팅한다.

---

## 주의

이 커맨드는 Worker가 자동으로 호출한다. 터미널에서 직접 Q&A를 원하면 `/dev:review-plan`을 사용한다.