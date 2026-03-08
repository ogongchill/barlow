# Phase 0~3 — 분석 & 계획

---

## Phase 0 — 스킬 & 규칙 로드

1. `workflow-guide` 스킬 로드 (이미 로드됨)
2. `coding-rules` 스킬 로드
3. `docs/DOMAIN_ENCYCLOPEDIA.md` 읽기 — **지연 로드 원칙**:
   - 이슈에서 BC명이 명확히 식별되면 → **해당 BC 섹션만** 읽는다
   - BC 불명확, 신규 BC 생성, BC 간 의존성 확인 필요 시에만 전량 로드
4. 요구사항의 작업 유형 판단 → 해당 규칙 파일 추가 로드

| 작업 유형 | 추가 로드 |
|---|---|
| 도메인 클래스(AR/VO/Port) | `ARCHITECTURE.md` + `DOMAIN_RULES.md` |
| core:service | `ARCHITECTURE.md` + `SERVICE_RULES.md` |
| API 엔드포인트 | `SERVICE_RULES.md` |
| 예외 | `ERROR_HANDLING.md` |
| 테스트 | `TESTING.md` |
| 전체 기능 구현 | 모든 파일 |

---

## Phase 1 — 요구사항 분석

### 1-1. 도메인 용어 추출
- 요구사항에서 도메인 명사를 추출한다
- `docs/DOMAIN_ENCYCLOPEDIA.md`의 빠른 참조 테이블에서 정확한 코드 클래스명·패키지명 확인
- 모호한 용어 → Encyclopedia의 "혼동 주의" 섹션 대조

### 1-2. 영향 BC 식별
- 어떤 Bounded Context가 영향을 받는지 목록화
- BC 간 의존 관계 확인 (DOMAIN_ENCYCLOPEDIA.md 의존 관계 섹션 참조)

### 1-3. 구현 위치 결정
각 구현 요소별 정확한 파일 경로를 사전에 결정한다:

```
[ ] core:domain/{bc}/        → 새 AR/VO/Repository/Command/Query?
[ ] core:service/{bc}/       → 새 Service/Reader/Manager?
[ ] infra:storage/           → 새 JpaEntity/Adapter?
[ ] app:api/controller/v1/   → 새 Controller/Request/Response?
[ ] 테스트 파일 위치?
```

### 1-4. 복잡도 판단 — Phase 2/3 파일 작성 여부 결정

다음 기준으로 작업 복잡도를 판단한다:

| 복잡 조건 (하나라도 해당 시 → 복잡) | 판단 |
|---|---|
| 3개 이상 레이어 변경 (domain + service + api) | 복잡 |
| 2개 이상 BC 영향 | 복잡 |
| 신규 AR 또는 Port Interface 설계 포함 | 복잡 |
| 예상 변경 파일 5개 이상 | 복잡 |

- **단순 작업**: Phase 2/3에서 채팅 출력만 사용 (파일 작성 생략)
- **복잡 작업**: Phase 2에서 `탐색결과.md`, Phase 3에서 `구현계획.md` 작성

### 1-5. 브랜치 전략 결정

예상 변경 파일 수를 기준으로 브랜치 전략을 결정한다.

```
예상 파일 수 ≤ 12개
  → 단일 브랜치: develop에서 feat/issue/{번호} 분기
    PR 1개 → develop

예상 파일 수 > 12개
  → 서브 브랜치 전략:
      develop에서 feat/issue/{번호} (베이스) 분기
      베이스에서 feat/issue/{번호}/domain 분기
      베이스에서 feat/issue/{번호}/service 분기
      베이스에서 feat/issue/{번호}/storage 분기
      베이스에서 feat/issue/{번호}/api 분기
    서브 PR → 베이스 브랜치 순차 머지 → 최종 PR → develop
```

**이 결정을 구현계획.md의 "브랜치 전략" 섹션에 반드시 명시한다.**

---

## Phase 2 — 코드베이스 탐색

### 2-1. 관련 기존 코드 파악
요구사항 구현에 필요한 기존 코드를 탐색한다:
- 동일 BC의 기존 AR, Repository, Service 읽기
- 유사한 기능 구현체 패턴 파악 (참고할 코드 찾기)
- 기존 예외 클래스 확인 (신규 추가 필요 여부)

### 2-2. 의존성 확인
- 새 클래스가 사용할 기존 Port Interface 목록 확인
- build.gradle 의존성이 이미 선언되어 있는지 확인

### 2-3. [복잡 작업만] 탐색결과.md 작성

탐색이 끝나면 아래 형식으로 `.claude/workspace/탐색결과.md`에 작성한다.
파일은 매 작업마다 덮어쓴다 (최신 1개만 유지).

```markdown
# 탐색 결과

## 요구사항 요약
{한 줄 요약}

## 영향 BC 및 파일 목록

### {BC명}
- `{파일경로}`: {현재 역할 / 변경이 필요한 이유}
- `{파일경로}`: ...

## 재사용 가능한 기존 패턴
- {패턴 설명}: `{참조 파일경로}`

## 신규 생성 필요 항목
- [ ] `{파일경로}`: {역할 한 줄}

## 주의사항 / 의존성
- {빌드 의존성, 순환 참조 위험 등}
```

작성 후 다음 두 가지를 수행한다:
1. MEMORY.md의 `# currentWork` 섹션을 아래 형식으로 업데이트한다:
   ```
   # currentWork
   - 이슈: #{번호} {제목}
   - 브랜치: {브랜치명}
   - 단계: Phase 2 완료 (탐색결과.md 작성됨)
   - 파일: .claude/workspace/탐색결과.md
   ```
2. 채팅에 "탐색결과.md를 작성했습니다. 확인 후 계속 진행하겠습니다." 메시지를 출력한다.

---

## Phase 3 — 구현 계획 (Plan) — 피드백 루프

### 3-1. [단순 작업] 채팅 출력 방식

계획을 채팅에 출력하고 사용자 승인을 기다린다:

```
## 구현 계획

### 변경 범위
- `{파일경로}`: {변경 내용 한 줄}

### 설계 결정
- {결정 사항}: {선택 이유}

진행할까요?
```

### 3-2. [복잡 작업] 구현계획.md 작성 방식

`.claude/workspace/구현계획.md`에 아래 형식으로 작성한다.
파일은 매 작업마다 덮어쓴다 (최신 1개만 유지).

```markdown
# 구현 계획

## 요구사항 요약
{한 줄 요약}

## 변경 범위
- [ ] `{파일경로}`: {변경 내용 한 줄}
- [ ] `{파일경로}`: {변경 내용 한 줄}

## 구현 순서
1. core:domain — {변경 내용}
2. core:service — {변경 내용}
3. infra:storage — {변경 내용}
4. app:api — {변경 내용}
5. 테스트 — {내용}

## 핵심 설계 결정
- {결정 1}: {선택한 방향과 이유}
- {결정 2}: ...

## 아키텍처 검증
- [ ] core:domain — 순수 POJO 유지 (Spring 어노테이션 없음)
- [ ] core:service — infra:* 직접 참조 없음
- [ ] 불변 객체 패턴 준수 (setter 없음)
- [ ] 예외 계층 규칙 준수 (CoreDomainException 하위)

## 테스트 계획
- [ ] 도메인 정책 단위 테스트 (태그 없음) — SUT 명확화, 경계값 중심
- [ ] 인수 테스트 (@AcceptanceTest → @Tag("context")) — 유스케이스 end-to-end
- 비즈니스 레이어 Mock 단위 테스트 작성 안 함 (AcceptanceTest가 커버)
- Repository 테스트 작성 안 함 (AcceptanceTest가 DB까지 커버)

## 커밋 계획
1. `feat : {내용}` — {대상 파일}
2. `feat : {내용}` — {대상 파일}
3. `test : {내용}` — {대상 파일}
4. `lint : lint 적용`

## 브랜치 전략
- 브랜치 종류: 단일 브랜치 / 서브 브랜치
- 기준 브랜치: develop
- 작업 브랜치: feat/issue/{번호} [또는 서브 브랜치 목록]
- PR 대상: develop [또는 feat/issue/{번호} 베이스]
```

작성 후 다음 두 가지를 수행한다:
1. MEMORY.md의 `# currentWork` 섹션을 업데이트한다:
   ```
   # currentWork
   - 이슈: #{번호} {제목}
   - 브랜치: {브랜치명}
   - 단계: Phase 3 완료 — 사용자 승인 대기 중
   - 파일: .claude/workspace/구현계획.md
   ```
2. 채팅에 다음 메시지를 출력한다:
   ```
   구현계획.md를 작성했습니다 (.claude/workspace/구현계획.md).

   수정이 필요하면 파일을 직접 편집 후 "진행해"라고 말씀해 주세요.
   이대로 진행해도 되면 "진행해"라고 말씀해 주세요.
   ```

사용자 승인 전까지 코드 작성을 시작하지 않는다.

---

## [세션 격리 체크포인트 A]

> **Phase 3 완료 & 사용자 승인 직후 권장**
>
> 계획 세션과 구현 세션을 분리하면 Phase 4 진입 시 컨텍스트가 구현에만 집중된다.
>
> ```
> 1. 구현계획.md 내용을 확인한다
> 2. /clear 로 대화 이력을 초기화한다
> 3. 새 세션에서 "Phase 4 진행해 — 이슈 #{번호}" 로 재개한다
> ```
>
> Claude는 MEMORY.md의 `# currentWork`에서 맥락을 복원한다.