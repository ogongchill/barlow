# Barlow — Claude 작업 지침

> 이 파일은 Claude가 이 프로젝트에서 작업할 때 반드시 따라야 하는 모든 규칙을 정의한다.

---

## 0. 작업 시작 — 이슈/요구사항 접수 시 가장 먼저 실행

### 이슈 Tier 판정 (자동)
모든 이슈는 구현 전에 Tier를 판정한다. 판정 기준은 `WORKFLOW.md Phase -1` 참조.

| Tier | 조건 | 진행 방식 |
|---|---|---|
| **Tier 1** | 단일 BC, 명확한 스펙, 파일 ≤12개, API/DB 계약 변경 없음 | 자율 진행 (승인 없이 Phase 8까지) |
| **Tier 2** | 멀티 BC, 기존 동작 변경, 파일 >12개, 모호한 요구사항 | 계획.md 작성 후 승인 대기 |
| **Tier 3** | 재설계, trade-off 결정, 대규모 리팩토링, ADR 필요 | 분석 보고서만. 코드 작성 금지 |

**오분류 원칙**: 항상 더 보수적인 Tier로. 애매하면 상위 Tier 선택.

### 커스텀 커맨드 (Tier 강제 지정)

| 커맨드 | 동작 |
|---|---|
| `/project:tier:dev-auto` | Tier 1 강제 자율 진행 (안전 조건 검사 후, 승인 없이 Phase 8까지) |
| `/project:tier:dev-plan` | Tier 2 계획 후 승인 모드 강제 |
| `/project:tier:dev-analyze` | Tier 3 사전 스크리닝 — Redesign/New Design 트랙 판별 후 입력 초안 도출 |

### 스킬 로드
**모든 코드 작성·수정·리뷰 작업 전에 아래 두 스킬을 순서대로 로드한다.**

1. `workflow-guide` — 개발 파이프라인 (항상 로드)
2. `coding-rules` — 도메인/서비스/API 코딩 규칙 (코드 작업 시 추가 로드)

`coding-rules` 스킬 로드 후 `SKILL.md`의 지시에 따라:
1. **무조건 첫 번째**: `docs/DOMAIN_ENCYCLOPEDIA.md` 읽기
2. 작업 유형에 맞는 추가 파일 로드 (ARCHITECTURE.md, DOMAIN_RULES.md 등)

---

## 1. 도메인 언어

`docs/DOMAIN_ENCYCLOPEDIA.md`에 한국어 용어 ↔ 코드 클래스 전체 매핑이 정의되어 있다.
요구사항에서 도메인 용어가 나오면 반드시 이 파일에서 먼저 확인한 후 코드를 작성한다.

---

## 2. 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.2.2
- **빌드**: Gradle (멀티 모듈)
- **ORM**: Spring Data JPA (MySQL, H2)
- **캐시**: Caffeine
- **테스트**: JUnit 5 + Spring Boot Test + REST Assured

---

## 3. 모듈 구조

```
app:api          — REST API 진입점 (bootJar)
app:batch        — 배치 작업 진입점 (bootJar)

core:domain      — 순수 도메인 (AR, VO, 인터페이스, 예외)
core:service     — 비즈니스 서비스 레이어

infra:storage              — JPA 영속성
infra:auth                 — 인증 (JWT, OIDC)
infra:notification         — 푸시 알림 (FCM)
infra:post-view            — 법안 조회수 처리
infra:clients:knal-api     — 국회 API 클라이언트

batch:batch-admin          — 배치 관리

support:logging            — 로깅 (Sentry + Slack)
support:monitoring         — 모니터링
support:alert              — 알림

tests:api-docs             — REST Docs 테스트 지원
```

### 모듈 간 의존 방향

```
app:api ──impl──> core:service ──impl──> core:domain
app:api ──impl──> core:domain
app:api ──runtime──> infra:storage (컴파일 타임에 infra 직접 참조 불가)

infra:storage ──compileOnly──> core:domain
infra:auth    ──compileOnly──> core:domain
```

---

## 4. 아키텍처 제약 — 절대 위반 금지

> 상세 규칙: `.claude/skills/coding-rules/ARCHITECTURE.md`

- `core:domain`: 순수 Java만. `@Service`, `@Component`, `@Transactional` 금지. 다른 모듈 의존 없음.
- `core:service`: `infra:*` 직접 import 금지. `core:domain` Repository 인터페이스만 주입.
- `infra:*`: `core:service` import 금지. `core:domain` Repository 인터페이스만 구현.
- `app:api`: `infra:*`는 `runtimeOnly`. 컨트롤러에서 infra 클래스 직접 import 금지.

---

## 5. 패키지 배치 규칙

> 상세 규칙: `.claude/skills/coding-rules/DOMAIN_RULES.md` + `SERVICE_RULES.md`

| 새 코드 종류 | 위치 |
|---|---|
| 도메인 AR/VO/Repository/Command/Query/Exception | `core/domain/src/.../core/domain/{bc}/` |
| Enum | `com.barlow.core.enumerate.*` |
| @Service (비즈니스 유스케이스) | `core/service/src/.../core/service/{bc}/business/` |
| @Component (Reader/Handler/Manager 등) | `core/service/src/.../core/service/{bc}/impl/` |
| JPA Entity/Repository/Adapter | `infra/storage/src/.../infra/storage/` |
| Controller/Request/Response | `app/api/src/.../app/api/controller/v1/{bc}/` |

---

## 6. 코딩 컨벤션

> 상세 규칙: `.claude/skills/coding-rules/DOMAIN_RULES.md`

- **불변 객체**: setter 금지. 상태 변경은 `with*()/activate()/deactivate()/modify*()` — 새 인스턴스 반환.
- **도메인 예외**: BC별 `{BC명}DomainException extends CoreDomainException` + static factory.
- **타입 선택**: AR→class, VO(행위 있음)→final class, VO(순수 데이터)/Read Model/Command/Query→record.
- **AR 간 참조**: ID-only (`long` 타입). 객체 직접 참조 금지.
- **Enum**: `getValue()` = 한국어 DB 표현, `name()` = 영어 상수, `findByValue(String)` = 외부→enum 변환.

---

## 7. 커밋 컨벤션

### 형식
```
{type} : {한국어 설명}
```

**space-colon-space** (` : `) 필수. 설명은 한국어.

### 타입 목록

| 타입 | 사용 시점 |
|---|---|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변화 없음) |
| `rename` | 클래스/파일명 변경 |
| `lint` | 포매터 적용 (`spotlessApply`) |
| `gradle` | 빌드 설정 변경 |
| `batch` | 배치 관련 변경 |
| `chore` | 기타 설정, 빌드 외 잡무 |
| `test` | 테스트 추가/수정 |
| `docs` | 문서 변경 |

### 커밋 단위 원칙
- **기능/목적 단위**로 커밋 분리
- lint 적용은 별도 커밋 (`lint : lint 적용`)
- 불필요한 포매팅 변경과 로직 변경을 같은 커밋에 섞지 않는다

---

## 8. 테스트 전략

> 상세 규칙: `.claude/skills/coding-rules/TESTING.md`

| 태그 | 실행 명령 | 의미 |
|---|---|---|
| 없음 | `./gradlew unitTest` | 순수 Java 단위 테스트 |
| `@Tag("develop")` | `./gradlew developTest` | Spring 컨텍스트 통합 테스트 |
| `@Tag("context")` | `./gradlew contextTest` | DB 컨텍스트 인수 테스트 (@AcceptanceTest) |
| `@Tag("restdocs")` | `./gradlew restDocsTest` | REST Docs 문서 생성 테스트 |

---

## 9. 빌드 & 린트 명령

```bash
./gradlew build                      # 전체 빌드
./gradlew spotlessApply              # 린트 적용 (Hook이 Java 파일 저장 시 모듈별 자동 실행)
./gradlew spotlessCheck              # 린트 검사 (CI)
./gradlew test                       # 기본 테스트
./gradlew unitTest                   # 순수 유닛 테스트
./gradlew developTest                # Spring 컨텍스트 통합 테스트
./gradlew contextTest                # DB 컨텍스트 테스트
./gradlew :core:domain:test          # 특정 모듈 테스트
```

### PostToolUse Hook 자동화

`.claude/settings.json`에 등록된 Hook이 Write/Edit 도구 실행 직후 **결정론적으로** 자동 실행된다.

| 자동화 항목 | 동작 | 스크립트 |
|---|---|---|
| **아키텍처 위반 즉시 감지** | Java 파일 저장 직후 금지 패턴 grep → 위반 시 즉시 채팅에 출력 | `.claude/hooks/post-write.sh` |
| **모듈별 spotlessApply** | 변경된 파일의 소속 모듈에 대해 spotlessApply 자동 실행 | `.claude/hooks/post-write.sh` |

감지하는 위반 패턴:
- `core:domain` — Spring/JPA 어노테이션 (`@Service`, `@Entity` 등), setter, `RuntimeException` 직접 상속
- `core:service` — `import com.barlow.infra.*`
- `app:api`, `app:batch` — `import com.barlow.infra.*`

### 코드 제출 전 체크리스트
1. `./gradlew spotlessApply` — Hook이 Java 파일마다 자동 적용. 전체 재확인 필요 시 수동 실행
2. `./gradlew test` — 테스트 통과 확인
3. 아키텍처 제약 자가 검토 (§4 참조) — Hook이 저장 시점에 이미 경고했을 경우 수정 여부 확인

---

## 10. 브랜치 & PR 규칙

### 기준 브랜치
**모든 작업은 `develop`에서 분기한다.** 서브 브랜치는 기능 베이스 브랜치에서 분기한다.

### 브랜치 네이밍
```
# 단일 브랜치
feat/issue/{이슈번호}           — 신규 기능
fix/issue/{이슈번호}            — 버그 수정
refactor/{설명 또는 #번호}      — 리팩토링
chore/{설명}                    — 설정, 의존성
docs/{설명}                     — 문서

# 서브 브랜치 (변경 파일 > 12개 시)
feat/issue/{이슈번호}/domain    — core:domain
feat/issue/{이슈번호}/service   — core:service
feat/issue/{이슈번호}/storage   — infra:storage
feat/issue/{이슈번호}/api       — app:api + 테스트
feat/issue/{이슈번호}/{기능명}  — 기능 단위 분할 시
```

### PR 크기 기준

| 기준 | 권장 | 최대 |
|---|---|---|
| 변경 파일 수 | ≤ 8개 | 12개 |
| 라인 추가(+) | ≤ 400줄 | 600줄 |

- **인접 레이어 혼재 허용**: domain + service, service + api
- **2단계 건너뛰기 금지**: domain + api 동시, domain + infra:storage 동시

### PR 생성 명령
```bash
# 단일 브랜치 PR
gh pr create --base develop --title "{제목}" --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"

# 서브 브랜치 PR (베이스 브랜치 지정)
gh pr create --base feat/issue/{번호} --title "{제목}" --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

> 상세 서브 브랜치 흐름: `.claude/skills/workflow-guide/WORKFLOW.md` Phase 8 참조

---

## 11. 작업 시 주의사항

### 하지 말 것
- `core:domain`에 `@Service`, `@Component`, `@Autowired` 추가
- `app:api` / `core:service`에서 `infra:*` 클래스 직접 import
- `setter` 메서드로 도메인 객체 상태 변경
- 단일 커밋에 린트 + 로직 변경 혼합
- `git push --force` (main 브랜치 절대 금지)

### 반드시 할 것
- 요구사항의 도메인 용어 → `docs/DOMAIN_ENCYCLOPEDIA.md`에서 먼저 확인
- 기능 단위로 커밋 분리 후 lint 커밋 별도 추가
- 새 BC/클래스 추가 시 `docs/DOMAIN_ENCYCLOPEDIA.md` 업데이트
- Hook이 아키텍처 위반을 경고했다면 → 다음 파일 작성 전에 반드시 수정

---

## 12. 참조 문서

| 문서 | 위치 | 내용 |
|---|---|---|
| 도메인 백과사전 | `docs/DOMAIN_ENCYCLOPEDIA.md` | 한국어 ↔ 코드 매핑, BC 정의, enum 전체 |
| 자율형 개발 파이프라인 | `.claude/skills/workflow-guide/WORKFLOW.md` | 이슈 트리어지 → PR 전 과정 (Phase -1~8) |
| 개발 Tier 커맨드 | `.claude/commands/tier/` (dev-auto, dev-plan, dev-analyze) | Tier 1/2/3 강제 지정 |
| 아키텍처 재설계 | `.claude/commands/pipeline/arch-redesign.md` | AS-IS 비판 → 대안 → ADR 생성 (Stage 0~6) |
| 신규 시스템 설계 | `.claude/commands/pipeline/arch-design.md` | 도메인 경계 → 패러다임 → 데이터 모델 → ADR (Stage 0~5) |
| 구현 계획 수립 | `.claude/commands/pipeline/arch-plan.md` | 코드 분해 → 계획 → `docs/plans/` 저장 (Step 0~3) |
| 하네스 검증 | `.claude/commands/harness-check.md` | 커밋 전 Barlow 규칙 준수 검증 |
| 아키텍처 규칙 | `.claude/skills/coding-rules/ARCHITECTURE.md` | 모듈 구조, 레이어 의존성 |
| 도메인 코딩 규칙 | `.claude/skills/coding-rules/DOMAIN_RULES.md` | 불변 패턴, 타입 선택, 패키지 |
| 서비스 코딩 규칙 | `.claude/skills/coding-rules/SERVICE_RULES.md` | Service/impl/Presentation |
| 예외 처리 규칙 | `.claude/skills/coding-rules/ERROR_HANDLING.md` | 예외 계층, HTTP 매핑 |
| 테스트 규칙 | `.claude/skills/coding-rules/TESTING.md` | 태그 분류, 작성 기준 |
| ADR | `docs/adr/` | 주요 아키텍처 결정 기록 |
| 배포 가이드 | `DEV-DEPLOY.md` | 개발 환경 배포 절차 |