#!/bin/bash
# PostToolUse Hook — Write/Edit 직후 자동 실행
#
# Hook 1: 아키텍처 위반 즉시 감지 (grep, 밀리초)
# Hook 2: 모듈별 spotlessApply (Gradle warm daemon ~1-2s)
#
# Claude Code가 stdin으로 tool 정보를 JSON 형식으로 전달한다.

set -euo pipefail

# ─── 파일 경로 추출 ──────────────────────────────────────────────
STDIN_DATA=$(cat)

FILE_PATH=$(echo "$STDIN_DATA" | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    # tool_input 중첩 형식
    fp = d.get('tool_input', {}).get('file_path', '')
    # flat 형식 fallback
    if not fp:
        fp = d.get('file_path', '')
    print(fp)
except Exception:
    pass
" 2>/dev/null)

# Java 파일이 아니면 종료
[[ -z "$FILE_PATH" ]] && exit 0
[[ "$FILE_PATH" != *.java ]] && exit 0
[[ ! -f "$FILE_PATH" ]] && exit 0

# 프로젝트 루트 탐지
PROJECT_ROOT=$(git -C "$(dirname "$FILE_PATH")" rev-parse --show-toplevel 2>/dev/null)
[[ -z "$PROJECT_ROOT" ]] && exit 0

RELATIVE_PATH="${FILE_PATH#"$PROJECT_ROOT"/}"

# ─── Hook 1: 아키텍처 위반 즉시 감지 ────────────────────────────

HEADER_PRINTED=false

print_header() {
    if [[ "$HEADER_PRINTED" == false ]]; then
        echo ""
        echo "━━━ [Barlow Arch Hook] $RELATIVE_PATH ━━━"
        HEADER_PRINTED=true
    fi
}

# ① core:domain 에 Spring/JPA 어노테이션 금지
if [[ "$RELATIVE_PATH" == core/domain/* ]]; then
    HITS=$(grep -nE \
        "@(Service|Component|Autowired|Transactional|Entity|Table|Column|Id|GeneratedValue|ManyToOne|OneToMany|OneToOne|ManyToMany|Embeddable|Embedded|Repository)" \
        "$FILE_PATH" 2>/dev/null || true)
    if [[ -n "$HITS" ]]; then
        print_header
        echo "❌ [ARCH] core:domain 에 Spring/JPA 어노테이션 발견 → 순수 POJO 유지 필수"
        echo "$HITS"
    fi
fi

# ② core:domain 에 setter 패턴 금지
if [[ "$RELATIVE_PATH" == core/domain/* ]]; then
    HITS=$(grep -n "public void set[A-Z]" "$FILE_PATH" 2>/dev/null || true)
    if [[ -n "$HITS" ]]; then
        print_header
        echo "❌ [DOMAIN] setter 발견 → with*/activate/deactivate/modify* 패턴 사용"
        echo "$HITS"
    fi
fi

# ③ core:domain 예외 클래스가 RuntimeException/Exception 직접 상속 금지
if [[ "$RELATIVE_PATH" == core/domain/* && "$FILE_PATH" == *Exception* ]]; then
    HITS=$(grep -nE "extends (RuntimeException|Exception)\b" "$FILE_PATH" 2>/dev/null || true)
    if [[ -n "$HITS" ]]; then
        print_header
        echo "❌ [ERROR] RuntimeException/Exception 직접 상속 → CoreDomainException 상속 필수"
        echo "$HITS"
    fi
fi

# ④ core:service 에서 infra:* 직접 import 금지
if [[ "$RELATIVE_PATH" == core/service/* ]]; then
    HITS=$(grep -n "import com\.barlow\.infra\." "$FILE_PATH" 2>/dev/null || true)
    if [[ -n "$HITS" ]]; then
        print_header
        echo "❌ [ARCH] core:service 에서 infra:* 직접 import → Port Interface 경유 필수"
        echo "$HITS"
    fi
fi

# ⑤ app:api 에서 infra:* 직접 import 금지
if [[ "$RELATIVE_PATH" == app/api/* || "$RELATIVE_PATH" == app/batch/* ]]; then
    HITS=$(grep -n "import com\.barlow\.infra\." "$FILE_PATH" 2>/dev/null || true)
    if [[ -n "$HITS" ]]; then
        print_header
        echo "❌ [ARCH] app 에서 infra:* 직접 import → runtimeOnly, 컴파일 타임 접근 금지"
        echo "$HITS"
    fi
fi

# ─── Hook 2: 모듈별 spotlessApply ───────────────────────────────

# 파일 경로에서 Gradle 모듈 경로 결정
if [[ "$RELATIVE_PATH" == core/domain/* ]]; then
    MODULE=":core:domain"
elif [[ "$RELATIVE_PATH" == core/service/* ]]; then
    MODULE=":core:service"
elif [[ "$RELATIVE_PATH" == infra/storage/* ]]; then
    MODULE=":infra:storage"
elif [[ "$RELATIVE_PATH" == infra/auth/* ]]; then
    MODULE=":infra:auth"
elif [[ "$RELATIVE_PATH" == infra/notification/* ]]; then
    MODULE=":infra:notification"
elif [[ "$RELATIVE_PATH" == infra/post-view/* ]]; then
    MODULE=":infra:post-view"
elif [[ "$RELATIVE_PATH" == infra/clients/knal-api/* ]]; then
    MODULE=":infra:clients:knal-api"
elif [[ "$RELATIVE_PATH" == app/api/* ]]; then
    MODULE=":app:api"
elif [[ "$RELATIVE_PATH" == app/batch/* ]]; then
    MODULE=":app:batch"
elif [[ "$RELATIVE_PATH" == batch/batch-admin/* ]]; then
    MODULE=":batch:batch-admin"
elif [[ "$RELATIVE_PATH" == support/logging/* ]]; then
    MODULE=":support:logging"
elif [[ "$RELATIVE_PATH" == support/monitoring/* ]]; then
    MODULE=":support:monitoring"
elif [[ "$RELATIVE_PATH" == support/alert/* ]]; then
    MODULE=":support:alert"
else
    exit 0
fi

cd "$PROJECT_ROOT"

if ./gradlew "${MODULE}:spotlessApply" -q 2>/dev/null; then
    print_header
    echo "✅ [spotless] ${MODULE} 포맷 적용 완료"
else
    print_header
    echo "⚠️  [spotless] ${MODULE} 포맷 실패 → 수동 실행: ./gradlew ${MODULE}:spotlessApply"
fi
