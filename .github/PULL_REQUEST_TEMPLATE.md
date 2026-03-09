## 개요

> 이 PR이 무엇을 하는지, 왜 필요한지 1~3줄로 설명한다.

<!-- 예) BillPost 조회 API에서 로그인하지 않은 사용자도 접근 가능하도록 권한 검사 로직을 제거한다. -->


## 관련 이슈

- Closes #


## 변경 유형

- [ ] `feat` — 새 기능
- [ ] `fix` — 버그 수정
- [ ] `refactor` — 리팩토링 (기능 변화 없음)
- [ ] `test` — 테스트 추가/수정
- [ ] `docs` — 문서
- [ ] `chore` — 설정, 빌드, 의존성


## 변경 레이어

> 변경된 레이어에 체크하고, 주요 변경 내용을 한 줄로 적는다.

- [ ] `core:domain` —
- [ ] `core:service` —
- [ ] `infra:storage` —
- [ ] `app:api` —
- [ ] `테스트` —


## 테스트

> 어떻게 검증했는지 작성한다. 실행한 명령어 또는 시나리오를 포함한다.

```bash
# 예)
./gradlew unitTest
./gradlew developTest
```


## 아키텍처 체크리스트

- [ ] `core:domain` 에 Spring 어노테이션(`@Service`, `@Component` 등) 없음
- [ ] `core:service` 에서 `infra:*` 직접 import 없음
- [ ] 도메인 객체 상태 변경 시 새 인스턴스 반환 (setter 없음)
- [ ] 새 예외는 `CoreDomainException` 또는 `CoreApiException` 하위 + static factory
- [ ] AR 간 참조는 ID-only (`long` 타입)


## 리뷰 포커스

> 리뷰어가 특히 집중해서 봐야 할 부분이나 의도적인 설계 결정을 적는다.

<!-- 예) SubscriptionActivator의 중복 구독 방지 로직이 도메인에 위치하는 것이 맞는지 검토 부탁드립니다. -->


---

<!-- 서브 브랜치 PR인 경우만 작성 -->
<details>
<summary>서브 PR 정보 (해당 시)</summary>

- 베이스 브랜치: `feat/issue/`
- 머지 순서: [ ] domain → [ ] service → [ ] storage → [ ] api
- 이 PR 위치:

</details>