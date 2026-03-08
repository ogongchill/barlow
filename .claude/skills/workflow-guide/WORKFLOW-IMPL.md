# Phase 4 — 코드 구현

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