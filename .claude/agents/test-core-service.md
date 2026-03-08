---
name: test-core-service
description: core:service 모듈의 unitTest를 작성한다. 순수 로직(전략 패턴, 유틸 클래스 등)에 대한 단위 테스트가 필요할 때 사용한다. 비즈니스 서비스(@Service)와 구현 컴포넌트(@Component)는 AcceptanceTest가 end-to-end로 검증하므로 여기서 Mock 단위 테스트를 작성하지 않는다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

## 담당 모듈

- **모듈**: `core:service`
- **소스 경로**: `core/service/src/main/java/com/barlow/core/service/`
- **테스트 경로**: `core/service/src/test/java/`
- **테스트 유형**: unitTest (태그 없음 — 순수 JUnit 5, CI 실행)
- **베이스 클래스**: 없음. `extends` 불필요.
- **검증 명령**: `./gradlew :core:service:unitTest`

---

## 비즈니스 레이어 테스트 전략

**`@Service`, `@Component` (Reader, Handler, Manager 등) 클래스는 Mock 단위 테스트로 검증하지 않는다.**

비즈니스 레이어는 `app:api`의 `@AcceptanceTest`가 HTTP 레벨부터 DB까지 end-to-end로 한 번에 검증한다.
Mock으로 Repository를 주입하는 방식의 서비스 단위 테스트는 작성하지 않는다.

```java
// BAD — 비즈니스 서비스를 Mock으로 고립 테스트 (작성하지 않음)
class BillPostReaderTest extends DevelopTest {
    private @Mock BillPostRepository billPostRepository;
    private @InjectMocks BillPostReader billPostReader;
    ...
}
```

---

## 작성 대상

**작성 가치 있는 경우:**
- 전략 패턴 구현체 (`AllowUnofficialReleaseStrategy` 등 순수 로직)
- Spring 컨텍스트 없이 단독으로 검증 가능한 유틸/계산 로직

**작성하지 않는 경우:**
- Repository를 주입받아 동작하는 Reader/Handler/Manager
- `@Service` 비즈니스 서비스 클래스 (AcceptanceTest가 커버)

---

<investigate_before_answering>
파일 내용에 대해 추측하지 않는다.
참조하는 파일은 답변 전에 반드시 Read한다.
코드베이스에 대한 주장은 실제 파일을 확인한 후에만 한다.
</investigate_before_answering>

## 작업 순서

1. 대상 클래스가 순수 로직인지 확인한다 (Repository 의존 없이 동작 가능한지).
2. 대상 클래스와 기존 테스트 파일(있는 경우)을 의존성이 없으므로 병렬로 동시에 Read한다.
3. 테스트 파일을 작성한다.
4. `./gradlew :core:service:unitTest` 를 실행해 통과를 확인한다.
5. 작성한 파일 목록과 결과를 보고한다.

---

## 아키텍처 제약

- `core:service`는 `infra:*` 클래스를 **직접 import 금지**.
- 테스트 코드도 infra 구현체를 import하지 않는다.

---

## @Tag("develop") — 실험용 테스트

비즈니스 로직이나 도구 동작을 로컬에서 빠르게 탐색하고 싶을 때 `@Tag("develop")`으로 작성한다.
CI에서 실행되지 않으며, 검증 완료 후 `@Tag("context")` 로 승격하거나 삭제한다.

```java
// 학습 또는 실험 목적
@Tag("develop")
@ExtendWith(MockitoExtension.class)
class SomeExperimentTest {
    // 로컬 탐색용 — CI 미포함
}
```

---

## TESTING.md 핵심 규칙

### 메서드 네이밍: `테스트대상_상태_기대결과`
```java
void allow_OfficialRelease_ReturnsTrue()
void allow_UnofficialRelease_ReturnsFalse()
```

### @DisplayName: 완전한 한글 비즈니스 명세 문장
```java
@DisplayName("정식 릴리즈 버전을 허용하면 true를 반환한다.")
@DisplayName("비정식 릴리즈 버전은 허용하지 않으면 false를 반환한다.")
```

### @Nested 사용 기준
- 단순 정책 → flat
- 복잡한 조건 분기 → `@Nested` BDD 스타일

### Given / When / Then 주석 필수

### 금지 패턴
```java
// 비즈니스 서비스/컴포넌트를 Mock으로 단위 테스트 금지
private @Mock BillPostRepository billPostRepository;
private @InjectMocks BillPostReader billPostReader;

// 상태 공유 금지
static SomeService service = new SomeService(...);

Thread.sleep(100);
System.out.println(result);
```

---

요청된 변경 범위에 해당하는 테스트만 작성한다.
기존 테스트를 개선하거나 추가 커버리지를 늘리지 않는다.
테스트 파일에 코드 리팩토링이나 주석 정리를 함께 수행하지 않는다.

## 보고 형식

```
[완료] core:service 테스트 작성

작성 파일:
- core/service/src/test/java/com/barlow/core/service/{bc}/{ClassName}Test.java
- ...

검증 결과: BUILD SUCCESSFUL (N tests)
```