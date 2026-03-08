# 자율형 개발 파이프라인 (Workflow)

요구사항 접수부터 PR 생성까지 Claude가 자율적으로 수행하는 전체 흐름을 정의한다.

> **단계 명칭**: 이 파이프라인의 단계는 **Phase N**으로 지칭한다.
> `/arch-redesign`, `/arch-design` 커맨드는 **Stage N**, `/arch-plan` 커맨드는 **Step N**을 사용한다.

---

## Phase -1 — 이슈 트리어지 (Tier 판정)

**모든 이슈는 구현 전에 반드시 Tier를 판정한다. Tier에 따라 이후 흐름이 완전히 달라진다.**

### 판정 기준 — 5가지 축

| 판단 축 | 안전 (낮은 Tier) | 위험 (높은 Tier) |
|---|---|---|
| 변경 방향 | 순수 추가 (새 코드만) | 기존 동작 수정·삭제·이동 |
| 아키텍처 영향 | 단일 BC 내부 | BC 경계 변경, 모듈 추가·제거 |
| 가역성 | 쉽게 롤백 가능 | DB 스키마, API 계약, 데이터 마이그레이션 |
| 요구사항 완결성 | 명확한 스펙 | 탐색적 ("어떻게 해야 할까?") |
| 결정 주체 | 구현 방법 결정 | 기술 방향 결정 (trade-off) |

### Tier 정의

#### Tier 1 — 자율 진행 (Autonomous)
다음 5가지 **ALL** 충족 시:
- [ ] 단일 BC만 영향
- [ ] 기존 API 계약 변경 없음 (엔드포인트 추가만 허용)
- [ ] DB 스키마 변경 없거나 순수 컬럼 추가만
- [ ] 이슈에 명확한 스펙 존재 (기대 동작, 제약 조건)
- [ ] 예상 변경 파일 ≤ 12개

→ Phase 0부터 사용자 승인 없이 Phase 8까지 자율 진행. 단, 시작 전 Tier 판정 결과를 채팅에 출력한다.
→ "자율"의 의미: Claude가 구현 결정을 스스로 내린다는 뜻. 트리거는 여전히 수동 (개발자가 이슈 내용을 Claude에게 전달).

#### Tier 2 — 계획 후 승인 (Plan & Approve)
다음 중 하나라도 해당 시:
- 영향 BC 2개 이상
- 기존 동작 변경 (순수 추가 아님)
- API 계약 변경 (Request/Response 구조 변경)
- 예상 변경 파일 > 12개
- 요구사항 일부 모호
- 단순 리팩토링 (범위 명확한 코드 구조 개선)

**일반 Tier 2**: Phase 0~3 실행 후 `.claude/workspace/구현계획.md` 작성 → 사용자 승인 대기 → 승인 후 Phase 4~8 진행.

**복잡 Tier 2** (코드베이스 깊은 분해가 필요한 경우):
→ `/project:pipeline:arch-plan {topic}` 실행 → `docs/plans/{날짜}-{topic}.md` 저장
→ 사용자 "확정" 후 **이 파이프라인 Phase 4 (코드 구현)** 로 복귀하여 진행.

승인/확정 없이 코드 작성 금지.

#### Tier 3 — 분석만 (Analyze Only)
다음 중 하나라도 해당 시:
- 아키텍처 재설계 (모듈 구조, BC 경계 재정의)
- 기술 trade-off 결정 필요 (기술 스택, 성능 vs 복잡성)
- 대규모 리팩토링 (다수 모듈 동시 영향)
- 레거시 청산 / 기술 부채 해소 (마이그레이션 계획 필요)
- ADR 작성이 선행되어야 하는 사안
- 보안 민감 변경 (인증, 권한, 암호화)
- 이슈 본문에 "재설계", "검토 필요", "결정 필요", "trade-off", "어떻게 해야" 포함

→ `/project:tier:dev-analyze` 실행 → **Redesign / New Design 트랙 판별** → 입력 초안 도출 → 사용자 확인
→ [기존 개선] 확인 후 `/project:pipeline:arch-redesign` 실행 → ADR 저장 → **코드 작성 일체 금지.**
→ [신규 구축] 확인 후 `/project:pipeline:arch-design` 실행 → ADR 저장 → **코드 작성 일체 금지.**
→ ADR 승인 후 구현이 필요하면: `/project:pipeline:arch-plan {topic} adr={ADR경로}` 실행
→ arch-plan Task 확정 후 **이 파이프라인 Phase 4 (코드 구현)** 로 복귀하여 진행.

### 이슈 본문 신호 탐지

```
Tier 1 신호: [FIX], [FEAT], "기대 동작:", "재현 방법:", 특정 클래스 1~2개 언급
Tier 2 신호: "개선", "정리", "최적화", 여러 파일 동시 언급, 범위 명확한 "리팩토링"
Tier 3 신호: "재설계", "마이그레이션", "전략", "trade-off", "ADR",
             "레거시", "기술 부채", "어떻게 할지", 다수 모듈 동시 언급,
             "신규 구축", "처음부터 만들기", "새 시스템", "없던 기능을 만든다"
```

### 판정 원칙
- **오분류 방향**: 항상 더 보수적인 Tier로. Tier 1 vs 2 애매 → Tier 2, Tier 2 vs 3 애매 → Tier 3
- **오버라이드**: 개발자가 `/project:tier:dev-auto`, `/project:tier:dev-plan`, `/project:tier:dev-analyze` 커맨드로 명시적 지정 가능

### Tier 판정 결과 출력 형식

```
## 이슈 Tier 판정

- 이슈: #{번호} {제목}
- 판정: Tier {1/2/3}
- 근거: {판정 이유 2~3줄}
- 진행 방식: {Autonomous 자율 진행 / 계획 후 승인 대기 / 분석 보고서만}

[Tier 3인 경우]
→ 코드 작성을 진행하지 않습니다.
  방향 결정 후 구체적인 구현 이슈를 별도로 만들어 주세요.
```

---

## Phase 0 — 스킬 & 규칙 로드

1. `workflow-guide` 스킬 로드 (이미 로드됨)
2. `coding-rules` 스킬 로드
3. `docs/DOMAIN_ENCYCLOPEDIA.md` 읽기 (필수 선행 — 예외 없음)
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

작성 후 채팅에 "탐색결과.md를 작성했습니다. 확인 후 계속 진행하겠습니다." 메시지를 출력한다.

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

작성 후 채팅에 다음 메시지를 출력한다:

```
구현계획.md를 작성했습니다 (.claude/workspace/구현계획.md).

수정이 필요하면 파일을 직접 편집 후 "진행해"라고 말씀해 주세요.
이대로 진행해도 되면 "진행해"라고 말씀해 주세요.
```

사용자 승인 전까지 코드 작성을 시작하지 않는다.

---

## Phase 4 — 코드 구현

### 4-1. 구현 순서
아래 순서를 지킨다 (의존성 방향 준수):

```
1. core:domain  — AR/VO/Command/Query/Repository Interface/Exception
2. core:service — Implement Layer (Reader/Manager/Activator 등)
3. core:service — Business Layer (Service/Facade)
4. infra:storage — JpaEntity/JpaRepository/RepositoryAdapter
5. app:api      — Controller/Request/Response
```

### 4-2. 구현 체크리스트 (각 파일 작성 후 확인)

**core:domain 파일:**
- [ ] 클래스 타입 선택 올바름 (AR=class, VO행위=final class, 순수VO/ReadModel/Command=record)
- [ ] setter 없음. 상태 변경 = 새 인스턴스 반환
- [ ] static factory 메서드로 생성 (생성자 private/package-private)
- [ ] Spring 어노테이션 없음
- [ ] AR 간 참조 = ID-only (long 타입)

**core:service 파일:**
- [ ] Business Layer(@Service): 모든 public 메서드에 @Transactional
- [ ] Business Layer: infra:* 직접 주입 없음
- [ ] Business Layer: 다른 Service 직접 주입 없음 (Facade로 조합)
- [ ] Implement Layer(@Component): Port Interface만 통해 infra 통신

**예외:**
- [ ] BC별 `{BC명}DomainException extends CoreDomainException` 구조
- [ ] static factory 메서드로만 생성
- [ ] `BUSINESS` vs `IMPLEMENTATION` 레벨 올바르게 설정

**API:**
- [ ] Request → Command 변환 (Service에 Request 직접 전달 금지)
- [ ] `request.validate()` 호출 (Controller 진입 시점)
- [ ] `Passport` 파라미터로 인증 사용자 주입

---

## Phase 5 — 테스트 작성

### 5-1. 테스트 전략

**비즈니스 레이어(`@Service`, Reader, Handler 등) Mock 단위 테스트는 작성하지 않는다.**
**Repository 테스트는 별도로 작성하지 않는다.**
인수 테스트가 HTTP → Service → DB 전체를 end-to-end로 검증한다.

| 테스트 종류 | 대상 | Agent | CI |
|---|---|---|---|
| 도메인 정책 테스트 (unitTest) | 상태 전이 불변 규칙, Policy, 계산 로직 | `test-core-domain` | O |
| 인수 테스트 (@AcceptanceTest) | 사용자 여정 end-to-end | `test-app-api` | O |
| 순수 로직 테스트 (unitTest) | core:service 전략 클래스 등 | `test-core-service` | O |
| API 문서화 테스트 (RestDocsContextTest) | app:api 엔드포인트 스펙 문서화 | `test-app-api-docs` | X (api-docs.yml) |
| 인프라 라이브러리 테스트 | JWT, FCM 등 라이브러리 동작 | `test-infra-auth`, `test-infra-notification` | X |

### 5-2. Agent 병렬 실행

변경 범위에 따라 해당하는 agent를 **병렬로** 실행한다.

```
[항상] test-core-domain  ─┐
[항상] test-app-api       ├─ 병렬 실행 (Agent 툴 사용)
[해당 시] test-core-service ┘

[선택] test-infra-auth        ← infra:auth 변경이 있을 때만
[선택] test-infra-notification ← infra:notification 변경이 있을 때만
[선택] test-app-api-docs      ← app:api 엔드포인트 추가/변경 시
```

**병렬 실행 규칙**: 모듈이 다르면 의존성이 없으므로 동시에 실행 가능하다.
각 agent는 해당 모듈의 테스트를 작성하고 `./gradlew :{module}:unitTest` 또는 `contextTest`로 검증 후 보고한다.

### 5-3. 테스트 품질 체크리스트
- [ ] 메서드명: `테스트대상_상태_기대결과` 형식
- [ ] @DisplayName: 완전한 한글 문장 ("~할 때, ~하면, ~한다")
- [ ] given/when/then 주석 구분
- [ ] 테스트 간 상태 공유 없음 (static 상태 금지)
- [ ] VO는 무조건 실제 객체 (SemanticVersion 등 Mock 금지)
- [ ] 비즈니스 레이어 Mock 테스트 없음
- [ ] Repository 테스트 별도 없음
- [ ] 단순 정책 → flat / 복잡한 정책 → @Nested BDD

---

## Phase 6 — 린트 & 검증

```bash
# 1. 린트 적용
./gradlew spotlessApply

# 2. 단위 테스트 실행 (CI 포함 — 항상 실행)
./gradlew unitTest

# 3. 인수 테스트 실행 (CI 포함 — 항상 실행)
./gradlew contextTest

# 4. 컴파일 확인
./gradlew :core:domain:compileJava
./gradlew :core:service:compileJava
./gradlew :app:api:compileJava

# [선택] 로컬 실험 테스트 실행 (CI 미포함)
./gradlew developTest
```

> **태그 기준**: `unitTest` + `contextTest` = CI 실행. `developTest` = 로컬 실험 전용.

테스트 실패 시 → 원인 분석 후 코드 수정. 같은 실패를 반복 재시도하지 않는다.

### 6-5. Harness 규칙 검증 (최종 관문)

```bash
/project:harness-check  # 이번 세션 전체 변경 파일 대상
```

- FAIL 항목이 1개라도 있으면 커밋하지 않고 수정 후 재검증한다.
- PASS 확인 후 Phase 7 커밋으로 진행한다.

---

## Phase 7 — 커밋

### 커밋 단위 원칙
- **기능/목적 단위**로 분리. 하나의 커밋 = 하나의 변경 이유
- lint 변경은 반드시 별도 커밋 (항상 마지막)

### 커밋 순서 예시 (전체 기능 구현 시)

```bash
# 1. 도메인 레이어
git add core/domain/...
git commit -m "feat : {BC명} {기능} 도메인 모델 추가"

# 2. 서비스 레이어
git add core/service/...
git commit -m "feat : {BC명} {기능} 서비스 레이어 구현"

# 3. 영속성 레이어
git add infra/storage/...
git commit -m "feat : {BC명} {기능} JPA 어댑터 구현"

# 4. API 레이어
git add app/api/...
git commit -m "feat : {BC명} {기능} API 엔드포인트 추가"

# 5. 테스트
git add **/test/...
git commit -m "test : {BC명} {기능} 테스트 추가"

# 6. 린트 (항상 마지막)
./gradlew spotlessApply
git add -A
git commit -m "lint : lint 적용"
```

커밋 형식: `{type} : {한국어 설명}` (space-colon-space 필수)

---

## Phase 8 — 브랜치 & PR

### 8-1. 브랜치 네이밍 규칙

```
# 단일 브랜치 (기본)
feat/issue/{이슈번호}           — 신규 기능
fix/issue/{이슈번호}            — 버그 수정
refactor/{설명 또는 #번호}      — 리팩토링
chore/{설명}                    — 설정, 의존성 등
docs/{설명}                     — 문서

# 서브 브랜치 (파일 > 12개 시)
feat/issue/{이슈번호}/domain    — core:domain 변경
feat/issue/{이슈번호}/service   — core:service 변경
feat/issue/{이슈번호}/storage   — infra:storage 변경
feat/issue/{이슈번호}/api       — app:api + 테스트

# 기능 단위 분할 (유스케이스가 독립적일 때)
feat/issue/{이슈번호}/{기능명-kebab-case}
```

**모든 브랜치는 `develop`에서 분기한다. 서브 브랜치는 기능 베이스 브랜치에서 분기한다.**

### 8-2. PR 크기 기준

| 기준 | 권장 | 최대 | 초과 시 |
|---|---|---|---|
| 변경 파일 수 | ≤ 8개 | 12개 | 레이어 단위 분할 필수 |
| 라인 추가(+) | ≤ 400줄 | 600줄 | 기능 단위 분할 검토 |

**레이어 혼재 규칙:**
- 허용: 인접 레이어 동시 변경 (domain + service, service + api)
- 금지: 2단계 건너뛰기 (domain + api 동시), domain + infra:storage 동시

### 8-3. 단일 브랜치 PR 흐름

```
develop
  └── feat/issue/{번호}   →  PR →  develop
```

```bash
git checkout develop && git pull
git checkout -b feat/issue/{번호}

# ... 구현 & 커밋 ...

gh pr create \
  --base develop \
  --title "{type}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

### 8-4. 서브 브랜치 PR 흐름

```
develop
  └── feat/issue/{번호}          ← 빈 베이스 브랜치
        ├── feat/issue/{번호}/domain   → PR → feat/issue/{번호}  [1]
        ├── feat/issue/{번호}/service  → PR → feat/issue/{번호}  [2] domain 머지 후
        ├── feat/issue/{번호}/storage  → PR → feat/issue/{번호}  [3] service 머지 후
        └── feat/issue/{번호}/api      → PR → feat/issue/{번호}  [4] storage 머지 후
      feat/issue/{번호}          → PR → develop                  [5] 전체 완료 후
```

```bash
# 베이스 브랜치 생성
git checkout develop && git pull
git checkout -b feat/issue/{번호}
git push -u origin feat/issue/{번호}

# 서브 브랜치 생성 (레이어별)
git checkout -b feat/issue/{번호}/domain

# ... domain 구현 & 커밋 ...

# 서브 PR — base는 베이스 브랜치
gh pr create \
  --base feat/issue/{번호} \
  --title "feat: {BC명} {기능} 도메인 모델" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"

# 서브 PR 머지 확인 후 다음 서브 브랜치 진행
git checkout feat/issue/{번호} && git pull
git checkout -b feat/issue/{번호}/service
# ...
```

**규칙: 이전 서브 PR이 베이스 브랜치에 머지되기 전까지 다음 서브 PR을 오픈하지 않는다.**

### 8-5. 최종 PR 제목 규칙
- 70자 이하
- 예: `feat: 법안 북마크 기능 구현`, `fix: 조회수 캐시 누락 버그 수정`

---

## 피드백 루프 — 언제 멈추고 질문하는가

다음 상황에서는 **코드 작성을 멈추고** 사용자에게 확인을 구한다:

| 상황 | 이유 |
|---|---|
| 요구사항이 기존 BC 범위를 벗어나는 새 BC 필요 여부 불명확 | 아키텍처 결정 |
| 두 BC 중 어느 쪽에 로직을 배치할지 모호 | 도메인 경계 결정 |
| 새 infra:* 모듈 추가 필요 여부 불명확 | 모듈 확장 결정 |
| 기존 API 계약(Request/Response 구조) 변경 수반 | 하위 호환성 결정 |
| 테스트 실패 원인이 구현 외 요인(환경, 데이터) | 외부 요인 확인 필요 |

---

## 금지 사항

- 계획(Phase 3) 승인 없이 코드 작성 시작 — 금지
- 테스트 실패 시 같은 명령 반복 재시도 — 금지
- `git push --force` — 금지
- 아키텍처 규칙 검증 없이 커밋 — 금지
- `docs/DOMAIN_ENCYCLOPEDIA.md` 확인 없이 새 클래스명 결정 — 금지