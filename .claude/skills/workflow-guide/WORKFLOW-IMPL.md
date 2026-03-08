# Phase 4 — 코드 구현

> **세션 격리 후 진입 시 (복잡 작업) — Phase 4 시작 전 반드시 수행:**
> 1. `.claude/workspace/구현계획.md` 읽기 — 변경 범위, 구현 순서, 설계 결정, 커밋·브랜치 계획 확인
> 2. `.claude/workspace/탐색결과.md` 읽기 — 영향 파일 목록, 재사용 패턴, 주의사항 확인
> 3. `coding-rules` 스킬 로드 → 작업 유형에 맞는 규칙 파일 읽기
> 4. 구현계획.md의 "변경 범위"에 있는 기존 파일 필요 시 재독
>
> 단순 작업(세션 격리 없이 연속 진행한 경우)은 위 절차 생략.

---

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