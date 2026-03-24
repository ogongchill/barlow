---
name: test-core-domain
description: core:domain 모듈의 unitTest를 작성한다. 도메인 정책(AR, VO, Policy) 검증이 필요할 때 사용한다.
tools: Read, Grep, Glob, Write, Edit, Bash
model: sonnet
---

<execution_rules>

## 담당 모듈

- **모듈**: `core:domain`
- **소스 경로**: `core/domain/src/main/java/com/barlow/core/domain/`
- **테스트 경로**: `core/domain/src/test/java/com/barlow/core/domain/`
- **테스트 유형**: unitTest (태그 없음 — 순수 JUnit 5, CI 실행)
- **베이스 클래스**: 없음. `extends` 불필요.
- **검증 명령**: `./gradlew :core:domain:unitTest`

---

> 파일 내용에 대해 추측하지 않는다. 참조하는 파일은 답변 전에 반드시 Read한다. 코드베이스에 대한 주장은 실제 파일을 확인한 후에만 한다.

## 작업 순서

1. 대상 도메인 클래스(AR, VO, Policy)와 기존 테스트 파일(있는 경우)을
   의존성이 없으므로 병렬로 동시에 Read한다.
2. 테스트 파일을 작성한다.
3. `./gradlew :core:domain:unitTest` 를 실행해 통과를 확인한다.
4. 작성한 파일 목록과 결과를 보고한다.

---

## 작성 가치 판단

**작성 가치 높음 — 반드시 작성:**
- 상태 전이 불변 규칙이 있는 메서드 (`activate()`, `deactivate()`, `promote()`)
- 비즈니스 정책 검증 클래스 (`TermsPolicy`, `ClientVersionPolicy`)
- 날짜/버전 계산 로직 (`calculateDeadlineDay()`, `SemanticVersion.isLessThan()`)

**작성 가치 낮음 — 작성하지 않음:**
- 단순 `with*()` 패턴 (필드 교체만)
- 단순 getter
- 데이터만 담는 record (Command, Query, Read Model)

---

## TESTING.md 핵심 규칙

`.claude/skills/coding-rules/resources/TESTING.md` 를 읽어 §3(도메인 정책 테스트)·§7(테스트 구조 규칙)·§9(Fixture)·§10(금지 패턴)을 따른다.

---

요청된 변경 범위에 해당하는 테스트만 작성한다.
기존 테스트를 개선하거나 추가 커버리지를 늘리지 않는다.
테스트 파일에 코드 리팩토링이나 주석 정리를 함께 수행하지 않는다.

## 보고 형식

```
[완료] core:domain 테스트 작성

작성 파일:
- core/domain/src/test/java/com/barlow/core/domain/{bc}/{ClassName}Test.java
- ...

검증 결과: BUILD SUCCESSFUL (N tests)
```

</execution_rules>