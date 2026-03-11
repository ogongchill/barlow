# Phase 5 — 커밋 & PR

---

## 커밋

**레이어 단위로 분리. lint는 항상 마지막.**

```bash
git add core/domain/...
git commit -m "feat : {BC명} {기능} 도메인 모델 추가"

git add core/service/...
git commit -m "feat : {BC명} {기능} 서비스 레이어 구현"

git add infra/...
git commit -m "feat : {BC명} {기능} 어댑터 구현"

git add app/...
git commit -m "feat : {BC명} {기능} API 엔드포인트 추가"

git add **/test/...
git commit -m "test : {BC명} {기능} 인수 테스트 추가"

./gradlew spotlessApply
git add -A
git commit -m "lint : lint 적용"
```

---

## PR

```bash
# 브랜치: {type}/issue/{번호} (항상 단일 브랜치, develop에서 분기)
gh pr create \
  --base develop \
  --title "{type}: {한국어 설명}" \
  --body "$(cat .github/PULL_REQUEST_TEMPLATE.md)"
```

PR 제목: 70자 이하. 예) `feat: 알림센터 7일 초과 항목 삭제 배치 잡 추가`

---

## 완료 후

MEMORY.md `# currentWork` 섹션 전체 삭제.

---

## 금지

- `git push --force` 금지
- harness-check PASS 없이 커밋 금지
- BC 경계·API 계약 변경 여부가 불명확하면 코드 작성 멈추고 사용자에게 확인