# Phase 7~8 — 커밋 & PR

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

> 모든 커밋 완료 후 MEMORY.md의 `# currentWork` 섹션을 현재 단계로 업데이트한다.
> PR 생성(Phase 8) 완료 후에는 `# currentWork` 섹션 전체를 삭제한다.

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

## [세션 격리 체크포인트 C]

> **Phase 8 완료 (PR 생성) 직후**
>
> ```
> 1. MEMORY.md의 # currentWork 섹션을 삭제한다
> 2. /clear 로 대화 이력을 초기화한다
> 3. 다음 이슈를 새 세션에서 시작한다
> ```

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