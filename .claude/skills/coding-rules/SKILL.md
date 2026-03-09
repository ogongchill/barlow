# coding-rules

## 트리거
도메인/서비스/API 코드를 작성·수정·리뷰·테스트할 때 이 스킬을 로드한다.

---

## 복잡도 판단 — 작업 시작 전 먼저 결정

다음 중 하나라도 해당하면 **복잡 작업**:
- 레이어 3개 이상 변경 (domain + service + api 등)
- BC 2개 이상 영향
- 신규 AR 또는 Port Interface 설계 포함
- 예상 변경 파일 5개 이상

**단순 작업**: 아래 표에서 해당 행의 파일만 로드
**복잡 작업**: `workflow-guide` 스킬을 먼저 로드 후, 아래 표 파일 추가 로드

## 작업별 추가 로드 파일

| 작업 | 로드할 파일 |
|------|-----------|
| 새 도메인 클래스(AR, VO, Port) 생성·수정 | `ARCHITECTURE.md` → `DOMAIN_RULES.md` |
| core:service (Service, Facade, Reader 등) 작성 | `ARCHITECTURE.md` → `SERVICE_RULES.md` |
| API 엔드포인트(Controller, Request, Response) 작성 | `SERVICE_RULES.md` |
| 예외 추가·수정 | `ERROR_HANDLING.md` |
| 테스트 작성 | `TESTING.md` |
| 전체 기능 구현 (도메인 → 서비스 → API) | `ARCHITECTURE.md` → `DOMAIN_RULES.md` → `SERVICE_RULES.md` → `ERROR_HANDLING.md` |

> **TESTING.md 로드 시점**: "테스트 작성" 단독 작업이 아닌 경우, TESTING.md는 Phase 5(테스트 작성) 진입 직전에 로드한다. Phase 0에서 선취하지 않는다.

---

## 파일 목록

```
ARCHITECTURE.md   — 모듈 구조, 레이어 의존성 방향, 4대 규칙
DOMAIN_RULES.md   — core:domain 코딩 규칙 전체 (불변, 타입 선택, 패키지 구조 등)
SERVICE_RULES.md  — core:service + app:api 구현 규칙
ERROR_HANDLING.md — 예외 계층, HTTP 매핑, 로깅/알럿 전략
TESTING.md        — 테스트 분류(태그), 작성 기준, 구조 규칙
```

---

## 핵심 원칙 요약

**Architecture:**
- 의존성 방향은 단방향 (app → core:service → core:domain ← infra).
- Business Layer는 Implement Layer(Reader/Manager)만 주입받는다. infra:* 직접 참조 금지.
- 새 기술 요구사항 → 새 `infra:` 모듈 추가. 기존 모듈에 기술 추가 금지.

**Domain:**
- Domain POJO = 순수 Java. @Entity, @Service, @Transactional 금지.
- 불변 객체: 상태 변경은 항상 새 인스턴스 반환 (with/activate/deactivate/modify*).
- 비즈니스 규칙은 Service가 아닌 Domain 객체 내부에 캡슐화 (Rich Domain Model).
- AR 간 참조는 ID-only (객체 직접 참조 금지).

**Error Handling:**
- Domain → `CoreDomainException` 하위 + static factory. HTTP 의존 금지.
- `BUSINESS` 레벨 → WARN. `IMPLEMENTATION` 레벨 → ERROR + Alert.
- `Exception` / `RuntimeException` 직접 상속 금지.

**Testing:**
- 도메인 정책 테스트: 태그 없음, 순수 Java, 실제 객체 우선, 경계값 중심.
- 통합 테스트: `@Tag("develop")` + `extends DevelopTest`.
- 메서드명: `테스트대상_상태_기대결과`. @DisplayName: 완전한 한글 비즈니스 명세 문장.