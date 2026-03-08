# Claude 프롬프트 엔지니어링 가이드

> 출처: [Anthropic Prompting Best Practices](https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/claude-prompting-best-practices)
> 대상 모델: Claude Opus 4.6, Claude Sonnet 4.6, Claude Haiku 4.5

---

## 목차

1. [기본 원칙](#1-기본-원칙)
2. [출력 및 포맷 제어](#2-출력-및-포맷-제어)
3. [도구 사용 (Tool Use)](#3-도구-사용-tool-use)
4. [사고 및 추론 (Thinking)](#4-사고-및-추론-thinking)
5. [에이전트 시스템](#5-에이전트-시스템)
6. [기능별 팁](#6-기능별-팁)
7. [마이그레이션 가이드](#7-마이그레이션-가이드)

---

## 1. 기본 원칙

### 1-1. 명확하고 직접적으로 지시하라

Claude는 명확하고 명시적인 지시에 잘 반응한다. "알아서 잘 해줘" 방식보다 **정확하게 원하는 것을 설명**할수록 결과가 좋아진다.

> **핵심 원칙**: 프롬프트를 처음 보는 동료에게 보여줬을 때 혼란스럽다면, Claude도 혼란스럽다.

Claude를 **맥락이 없는 똑똑한 신입 직원**으로 생각하라. 업무 방식, 기대치, 배경 지식을 모두 설명해야 한다.

**실천 방법:**
- 원하는 출력 형식과 제약 조건을 구체적으로 명시
- 순서나 완전성이 중요한 경우 번호 매기기/불릿 포인트로 단계를 제시
- "더 잘 해줘" 대신 구체적으로 무엇을 더 해야 하는지 명시

**예시 비교:**

```
# 나쁜 예 (모호함)
Create an analytics dashboard

# 좋은 예 (구체적)
Create an analytics dashboard. Include as many relevant features and interactions
as possible. Go beyond the basics to create a fully-featured implementation.
```

---

### 1-2. 지시 사항에 이유(Context)를 함께 제공하라

단순히 규칙을 나열하는 것보다 **왜 그 규칙이 필요한지 설명**하면 Claude가 의도를 이해하고 더 정확하게 반응한다. Claude는 설명으로부터 일반화할 수 있을 만큼 충분히 똑똑하다.

**예시 비교:**

```
# 나쁜 예 (이유 없는 금지)
NEVER use ellipses

# 좋은 예 (이유 포함)
Your response will be read aloud by a text-to-speech engine, so never use ellipses
since the text-to-speech engine will not know how to pronounce them.
```

---

### 1-3. 예시를 효과적으로 사용하라 (Few-Shot / Multishot Prompting)

**잘 만든 예시 몇 개(3~5개)** 는 정확도와 일관성을 극적으로 향상시킨다. 예시는 Claude의 출력 형식, 톤, 구조를 가장 안정적으로 유도하는 방법이다.

**좋은 예시의 특징:**
- **관련성 (Relevant)**: 실제 사용 케이스와 유사하게
- **다양성 (Diverse)**: 엣지 케이스를 커버하고, 의도하지 않은 패턴을 학습하지 않도록 충분히 다양하게
- **구조화 (Structured)**: `<example>` 태그 (복수 시 `<examples>`) 로 감싸서 지시 사항과 명확히 구분

```xml
<examples>
  <example>
    <input>...</input>
    <output>...</output>
  </example>
  <example>
    <input>...</input>
    <output>...</output>
  </example>
</examples>
```

> **팁**: Claude에게 예시의 관련성과 다양성을 평가하게 하거나, 초기 예시 세트를 기반으로 추가 예시를 생성하게 할 수 있다.

---

### 1-4. XML 태그로 프롬프트를 구조화하라

복잡한 프롬프트(지시, 맥락, 예시, 변수 입력이 혼합된 경우)에서 XML 태그는 Claude가 각 요소를 명확하게 구분하도록 돕는다.

**Best Practice:**
- 일관되고 서술적인 태그 이름 사용 (`<instructions>`, `<context>`, `<input>`)
- 계층 구조가 있을 때는 중첩 태그 사용

**긴 문서 처리 시 멀티 문서 구조 예시:**

```xml
<documents>
  <document index="1">
    <source>annual_report_2023.pdf</source>
    <document_content>
      {{ANNUAL_REPORT}}
    </document_content>
  </document>
  <document index="2">
    <source>competitor_analysis_q2.xlsx</source>
    <document_content>
      {{COMPETITOR_ANALYSIS}}
    </document_content>
  </document>
</documents>

Analyze the annual report and competitor analysis. Identify strategic advantages
and recommend Q3 focus areas.
```

---

### 1-5. Claude에게 역할을 부여하라

시스템 프롬프트에서 역할을 설정하면 Claude의 행동과 톤이 해당 용도에 맞게 집중된다. 한 문장만으로도 효과가 있다.

```python
client.messages.create(
    model="claude-opus-4-6",
    max_tokens=1024,
    system="You are a helpful coding assistant specializing in Python.",
    messages=[
        {"role": "user", "content": "How do I sort a list of dictionaries by key?"}
    ],
)
```

---

### 1-6. 긴 컨텍스트 프롬프팅 (20K+ 토큰)

긴 문서나 데이터가 많은 입력을 다룰 때의 구조화 전략:

| 전략 | 설명 | 효과 |
|---|---|---|
| **긴 데이터를 위에 배치** | 장문 문서/입력을 프롬프트 상단에, 쿼리/지시는 하단에 | 응답 품질 최대 30% 향상 |
| **XML 태그로 문서 구조화** | `<document>`, `<document_content>`, `<source>` 태그 활용 | 파싱 정확도 향상 |
| **인용 기반 응답** | 관련 부분을 먼저 인용하게(`<quotes>`) 한 후 작업 수행 | 노이즈 필터링, 정확도 향상 |

**의사 진단 예시 (인용 + 분석 분리):**

```xml
<documents>
  <document index="1">
    <source>patient_symptoms.txt</source>
    <document_content>{{PATIENT_SYMPTOMS}}</document_content>
  </document>
  <document index="2">
    <source>patient_records.txt</source>
    <document_content>{{PATIENT_RECORDS}}</document_content>
  </document>
</documents>

Find quotes from the patient records that are relevant to diagnosing the patient's
reported symptoms. Place these in <quotes> tags. Then, based on these quotes, list
all information that would help the doctor diagnose the patient's symptoms. Place
your diagnostic information in <info> tags.
```

---

## 2. 출력 및 포맷 제어

### 2-1. 소통 스타일과 상세도

최신 Claude 모델의 소통 특성 (이전 모델 대비):

| 특성 | 설명 |
|---|---|
| **더 직접적** | 자화자찬 없이 사실 기반의 진행 보고 |
| **더 대화적** | 덜 기계적, 더 자연스럽고 구어체적 |
| **덜 장황함** | 효율성을 위해 도구 사용 후 상세 요약 생략 가능 |

도구 사용 후 추론 과정을 보고 싶다면:
```
After completing a task that involves tool use, provide a quick summary of the work you've done.
```

---

### 2-2. 응답 포맷 제어 방법

**1. 하지 말라 대신 해야 할 것을 말하라:**
```
# 나쁜 예
Do not use markdown in your response

# 좋은 예
Your response should be composed of smoothly flowing prose paragraphs.
```

**2. XML 형식 지시자 사용:**
```
Write the prose sections of your response in <smoothly_flowing_prose_paragraphs> tags.
```

**3. 프롬프트 스타일을 원하는 출력 스타일에 맞추라:**
- 프롬프트에서 마크다운을 제거하면 출력의 마크다운도 줄어든다

**4. 마크다운/불릿 최소화 상세 프롬프트:**

```xml
<avoid_excessive_markdown_and_bullet_points>
When writing reports, documents, technical explanations, analyses, or any long-form
content, write in clear, flowing prose using complete paragraphs and sentences.
Use standard paragraph breaks for organization and reserve markdown primarily for
`inline code`, code blocks, and simple headings (### and ###).
Avoid using **bold** and *italics*.

DO NOT use ordered lists (1. ...) or unordered lists (*) unless:
a) you're presenting truly discrete items where a list format is the best option, or
b) the user explicitly requests a list or ranking

Instead of listing items with bullets or numbers, incorporate them naturally into
sentences. NEVER output a series of overly short bullet points.

Your goal is readable, flowing text that guides the reader naturally through ideas
rather than fragmenting information into isolated points.
</avoid_excessive_markdown_and_bullet_points>
```

---

### 2-3. LaTeX 출력 제어

Claude Opus 4.6은 수식 표현에 기본적으로 LaTeX를 사용한다. 일반 텍스트를 원하면:

```
Format your response in plain text only. Do not use LaTeX, MathJax, or any markup
notation such as \( \), $, or \frac{}{}. Write all math expressions using standard
text characters (e.g., "/" for division, "*" for multiplication, and "^" for exponents).
```

---

### 2-4. Prefilled Response 마이그레이션 (Claude 4.6부터 미지원)

Claude 4.6 모델부터 마지막 assistant turn의 prefilled response가 더 이상 지원되지 않는다.

| 기존 용도 | 마이그레이션 방법 |
|---|---|
| 특정 출력 포맷 강제 (JSON/YAML) | Structured Outputs 기능 사용, 또는 스키마 준수 명시적 지시 |
| 서두 제거 (`Here is the requested summary:`) | 시스템 프롬프트에 "Respond directly without preamble" 지시 |
| 불필요한 거부 회피 | 명확한 프롬프팅으로 충분 (최신 모델은 적절한 거부 판단) |
| 연속 응답 | user 메시지에 이전 응답 끝 텍스트 포함: "Your previous response ended with `[text]`. Continue from where you left off." |
| 컨텍스트 주입 | 매우 긴 대화에서는 user turn에 주입하거나 도구로 컨텍스트 제공 |

---

## 3. 도구 사용 (Tool Use)

### 3-1. 명시적 행동 지시

최신 Claude 모델은 정확한 지시 따르기 훈련을 받았다. "제안해줄 수 있어?" 라고 하면 제안만 하고 직접 변경하지 않는다.

**행동을 원한다면 명시적으로 지시:**

```
# 나쁜 예 (제안만 함)
Can you suggest some changes to improve this function?

# 좋은 예 (직접 변경)
Change this function to improve its performance.
```

**기본 행동 스타일 설정:**

```xml
<!-- 적극적 행동 기본값 -->
<default_to_action>
By default, implement changes rather than only suggesting them. If the user's intent
is unclear, infer the most useful likely action and proceed, using tools to discover
any missing details instead of guessing.
</default_to_action>

<!-- 보수적 행동 기본값 -->
<do_not_act_before_instructions>
Do not jump into implementation or change files unless clearly instructed to make
changes. When the user's intent is ambiguous, default to providing information,
doing research, and providing recommendations rather than taking action.
</do_not_act_before_instructions>
```

> **주의**: Claude Opus 4.5/4.6은 이전 모델보다 시스템 프롬프트에 더 민감하게 반응한다. "CRITICAL: You MUST use this tool when..." 같은 강한 표현은 과도한 트리거링을 유발할 수 있다. "Use this tool when..." 같은 일반적인 표현으로 완화하라.

---

### 3-2. 병렬 도구 호출 최적화

최신 Claude 모델은 병렬 도구 실행에 뛰어나다. 성능을 최대화하거나 조절하는 프롬프트:

**최대 병렬 효율:**
```xml
<use_parallel_tool_calls>
If you intend to call multiple tools and there are no dependencies between the tool
calls, make all of the independent tool calls in parallel. Prioritize calling tools
simultaneously whenever the actions can be done in parallel rather than sequentially.
For example, when reading 3 files, run 3 tool calls in parallel to read all 3 files
into context at the same time. Maximize use of parallel tool calls where possible
to increase speed and efficiency. However, if some tool calls depend on previous
calls to inform dependent values like the parameters, do NOT call these tools in
parallel and instead call them sequentially. Never use placeholders or guess missing
parameters in tool calls.
</use_parallel_tool_calls>
```

**순차 실행 (안정성 우선):**
```
Execute operations sequentially with brief pauses between each step to ensure stability.
```

---

## 4. 사고 및 추론 (Thinking)

### 4-1. 과도한 사고 (Overthinking) 방지

Claude Opus 4.6은 높은 `effort` 설정에서 이전 모델보다 훨씬 더 많은 사전 탐색을 수행한다. 이는 결과를 향상시키지만 latency와 토큰 비용을 증가시킬 수 있다.

**과도한 사고 억제 프롬프트:**
```
When you're deciding how to approach a problem, choose an approach and commit to it.
Avoid revisiting decisions unless you encounter new information that directly contradicts
your reasoning. If you're weighing two approaches, pick one and see it through. You can
always course-correct later if the chosen approach fails.
```

**도구 관련 과도한 트리거링 방지:**
- "Default to using [tool]" → "Use [tool] when it would enhance your understanding of the problem"
- "If in doubt, use [tool]" 같은 표현 제거
- 필요 시 `effort` 파라미터를 낮게 설정

---

### 4-2. 사고 모드 비교

| 모드 | 모델 | 설정 방법 | 특징 |
|---|---|---|---|
| **Adaptive Thinking** | Opus 4.6, Sonnet 4.6 | `thinking: {type: "adaptive"}` | Claude가 동적으로 사고 깊이 결정. 내부 평가에서 Extended보다 성능 우수 |
| **Extended Thinking** | Sonnet 4.6, 구형 모델 | `thinking: {type: "enabled", budget_tokens: N}` | 수동으로 사고 예산 설정 |
| **Thinking 없음** | 전 모델 | `thinking` 파라미터 생략 | 기본값 |

**Adaptive Thinking 마이그레이션:**
```python
# Before (extended thinking, 구형 모델)
client.messages.create(
    model="claude-sonnet-4-5-20250929",
    max_tokens=64000,
    thinking={"type": "enabled", "budget_tokens": 32000},
    messages=[{"role": "user", "content": "..."}],
)

# After (adaptive thinking)
client.messages.create(
    model="claude-opus-4-6",
    max_tokens=64000,
    thinking={"type": "adaptive"},
    output_config={"effort": "high"},  # max, high, medium, low 중 선택
    messages=[{"role": "user", "content": "..."}],
)
```

---

### 4-3. 사고 활용 Best Practice

| 기법 | 설명 |
|---|---|
| **일반 지시 선호** | "think thoroughly" 같은 일반 지시가 세부 단계 나열보다 더 나은 추론을 유발 |
| **Few-shot + thinking 결합** | 예시 내에 `<thinking>` 태그를 포함하면 Claude가 그 추론 패턴을 일반화 |
| **수동 CoT (thinking 꺼진 경우)** | `<thinking>`과 `<answer>` 태그로 추론과 최종 답변을 명확히 분리 |
| **자가 검증 요청** | "Before you finish, verify your answer against [test criteria]" 로 오류를 안정적으로 잡음 |
| **도구 사용 후 반성 유도** | "After receiving tool results, carefully reflect on their quality and determine optimal next steps before proceeding." |

> **주의**: Extended thinking이 비활성화된 경우, Claude Opus 4.5는 "think" 및 변형어에 특히 민감하다. "consider", "evaluate", "reason through" 같은 대안을 사용하라.

---

## 5. 에이전트 시스템

### 5-1. 장기 추론과 상태 추적

최신 Claude 모델은 장기 추론 작업에서 탁월한 상태 추적 능력을 가진다. 점진적 진행에 집중하며 여러 컨텍스트 윈도우에 걸친 복잡한 작업도 처리 가능하다.

**컨텍스트 자동 압축 환경에서의 프롬프트:**
```
Your context window will be automatically compacted as it approaches its limit,
allowing you to continue working indefinitely from where you left off. Therefore,
do not stop tasks early due to token budget concerns. As you approach your token
budget limit, save your current progress and state to memory before the context
window refreshes. Always be as persistent and autonomous as possible and complete
tasks fully, even if the end of your budget is approaching. Never artificially
stop any task early regardless of the context remaining.
```

---

### 5-2. 멀티 컨텍스트 윈도우 워크플로우

장기 작업에서 여러 컨텍스트 윈도우를 사용할 때의 전략:

| 전략 | 설명 |
|---|---|
| **첫 번째 컨텍스트에서 프레임워크 구축** | 테스트 작성, 셋업 스크립트 생성 후 이후 컨텍스트에서 TODO 리스트 기반 반복 |
| **구조화된 테스트 파일 생성** | `tests.json` 같은 구조화 형식으로 테스트 추적. "테스트 제거는 절대 금지" 명시 |
| **QoL 도구 셋업** | `init.sh` 같은 셋업 스크립트 생성으로 서버 시작, 테스트, 린터 실행 자동화 |
| **신선한 컨텍스트 시작** | 컴팩션 대신 완전 새 컨텍스트 시작 시 파일 기반 상태 복구 방향 제시 |
| **검증 도구 제공** | Playwright MCP, computer use 등 Claude가 독립적으로 정확성을 검증할 수 있는 도구 |

**신선한 컨텍스트 시작 시 지시 예시:**
```
Call pwd; you can only read and write files in this directory.
Review progress.txt, tests.json, and the git logs.
Manually run through a fundamental integration test before moving on to
implementing new features.
```

**긴 작업 완료 독려:**
```
This is a very long task, so it may be beneficial to plan out your work clearly.
It's encouraged to spend your entire output context working on the task - just make
sure you don't run out of context with significant uncommitted work. Continue working
systematically until you have completed this task.
```

---

### 5-3. 상태 관리 Best Practice

**구조화 vs. 비구조화 데이터:**

```json
// 구조화 상태 파일 (tests.json) — 스키마 요구사항이 있는 구조화 정보에 적합
{
  "tests": [
    { "id": 1, "name": "authentication_flow", "status": "passing" },
    { "id": 2, "name": "user_management", "status": "failing" },
    { "id": 3, "name": "api_endpoints", "status": "not_started" }
  ],
  "total": 200,
  "passing": 150,
  "failing": 25,
  "not_started": 25
}
```

```text
// 진행 메모 (progress.txt) — 일반 진행 상황과 컨텍스트 추적에 적합
Session 3 progress:
- Fixed authentication token validation
- Updated user model to handle edge cases
- Next: investigate user_management test failures (test #2)
- Note: Do not remove tests as this could lead to missing functionality
```

**상태 추적 도구:**
- **git**: 수행된 작업 로그 + 복원 가능한 체크포인트. 최신 Claude 모델이 특히 git을 활용한 상태 추적에 뛰어남
- **구조화 형식**: JSON 등 구조화 형식으로 테스트 결과/작업 상태 추적
- **진행 메모**: 자유 형식 텍스트로 일반 진행 상황 추적

---

### 5-4. 자율성과 안전성 균형

Claude Opus 4.6은 가이드 없이 파일 삭제, force-push, 외부 서비스 게시 같은 되돌리기 어려운 작업을 수행할 수 있다. 확인이 필요한 경우:

```
Consider the reversibility and potential impact of your actions. You are encouraged
to take local, reversible actions like editing files or running tests, but for actions
that are hard to reverse, affect shared systems, or could be destructive, ask the user
before proceeding.

Examples of actions that warrant confirmation:
- Destructive operations: deleting files or branches, dropping database tables, rm -rf
- Hard to reverse operations: git push --force, git reset --hard, amending published commits
- Operations visible to others: pushing code, commenting on PRs/issues, sending messages,
  modifying shared infrastructure

When encountering obstacles, do not use destructive actions as a shortcut. For example,
don't bypass safety checks (e.g. --no-verify) or discard unfamiliar files that may be
in-progress work.
```

---

### 5-5. 리서치 및 정보 수집

복잡한 리서치 태스크를 위한 구조적 접근 프롬프트:

```
Search for this information in a structured way. As you gather data, develop several
competing hypotheses. Track your confidence levels in your progress notes to improve
calibration. Regularly self-critique your approach and plan. Update a hypothesis tree
or research notes file to persist information and provide transparency. Break down this
complex research task systematically.
```

---

### 5-6. 서브에이전트 오케스트레이션

최신 모델은 명시적 지시 없이도 서브에이전트 오케스트레이션을 자연스럽게 수행한다. 그러나 Claude Opus 4.6은 단순한 grep으로 충분한 상황에서도 서브에이전트를 남용할 수 있다.

**서브에이전트 사용 기준 명시:**
```
Use subagents when tasks can run in parallel, require isolated context, or involve
independent workstreams that don't need to share state. For simple tasks, sequential
operations, single-file edits, or tasks where you need to maintain context across
steps, work directly rather than delegating.
```

---

### 5-7. 과도한 적극성 (Overeagerness) 억제

Claude Opus 4.5/4.6은 요청 이상의 파일을 생성하거나 불필요한 추상화를 만드는 경향이 있다.

**오버엔지니어링 방지 프롬프트:**
```xml
<avoid_overengineering>
Avoid over-engineering. Only make changes that are directly requested or clearly
necessary. Keep solutions simple and focused:

- Scope: Don't add features, refactor code, or make "improvements" beyond what was
  asked. A bug fix doesn't need surrounding code cleaned up.

- Documentation: Don't add docstrings, comments, or type annotations to code you
  didn't change. Only add comments where the logic isn't self-evident.

- Defensive coding: Don't add error handling, fallbacks, or validation for scenarios
  that can't happen. Trust internal code and framework guarantees.

- Abstractions: Don't create helpers, utilities, or abstractions for one-time
  operations. Don't design for hypothetical future requirements. The right amount of
  complexity is the minimum needed for the current task.
</avoid_overengineering>
```

---

### 5-8. 테스트 통과에 집중한 하드코딩 방지

```
Please write a high-quality, general-purpose solution using the standard tools
available. Do not create helper scripts or workarounds to accomplish the task more
efficiently. Implement a solution that works correctly for all valid inputs, not just
the test cases. Do not hard-code values or create solutions that only work for specific
test inputs. Instead, implement the actual logic that solves the problem generally.

Focus on understanding the problem requirements and implementing the correct algorithm.
Tests are there to verify correctness, not to define the solution. If the task is
unreasonable or infeasible, or if any of the tests are incorrect, please inform me
rather than working around them.
```

---

### 5-9. 환각 (Hallucination) 최소화

코드 관련 환각을 최소화하는 프롬프트:

```xml
<investigate_before_answering>
Never speculate about code you have not opened. If the user references a specific file,
you MUST read the file before answering. Make sure to investigate and read relevant files
BEFORE answering questions about the codebase. Never make any claims about code before
investigating unless you are certain of the correct answer - give grounded and
hallucination-free answers.
</investigate_before_answering>
```

---

### 5-10. 임시 파일 정리

Claude가 반복/테스트 목적으로 임시 파일을 생성할 수 있다. 정리를 원하면:
```
If you create any temporary new files, scripts, or helper files for iteration, clean up
these files by removing them at the end of the task.
```

---

## 6. 기능별 팁

### 6-1. 향상된 비전 기능

Claude Opus 4.5/4.6은 이전 모델 대비 향상된 이미지 처리 및 데이터 추출 능력을 가진다.

**성능 향상 기법:**
- **Crop 도구 제공**: 이미지의 관련 영역을 "확대(zoom)" 할 수 있는 crop 도구를 Claude에게 제공하면 일관된 성능 향상을 보임
- **멀티 이미지**: 여러 이미지가 있을 때 특히 성능이 좋음
- **비디오 분석**: 비디오를 프레임으로 분해하여 분석 가능

---

### 6-2. 프론트엔드 디자인

모델이 가이드 없이 기본 패턴으로 수렴하여 "AI slop" 외관을 만들 수 있다. 독창적인 프론트엔드를 위한 프롬프트:

```xml
<frontend_aesthetics>
You tend to converge toward generic, "on distribution" outputs. In frontend design,
this creates what users call the "AI slop" aesthetic. Avoid this: make creative,
distinctive frontends that surprise and delight.

Focus on:
- Typography: Choose fonts that are beautiful, unique, and interesting. Avoid generic
  fonts like Arial and Inter; opt instead for distinctive choices that elevate the
  frontend's aesthetics.
- Color & Theme: Commit to a cohesive aesthetic. Use CSS variables for consistency.
  Dominant colors with sharp accents outperform timid, evenly-distributed palettes.
  Draw from IDE themes and cultural aesthetics for inspiration.
- Motion: Use animations for effects and micro-interactions. Prioritize CSS-only
  solutions for HTML. Use Motion library for React when available. Focus on high-impact
  moments: one well-orchestrated page load with staggered reveals creates more delight
  than scattered micro-interactions.
- Backgrounds: Create atmosphere and depth rather than defaulting to solid colors.

Avoid generic AI-generated aesthetics:
- Overused font families (Inter, Roboto, Arial, system fonts)
- Clichéd color schemes (particularly purple gradients on white backgrounds)
- Predictable layouts and component patterns

Interpret creatively and make unexpected choices that feel genuinely designed for the
context. Vary between light and dark themes, different fonts, different aesthetics.
</frontend_aesthetics>
```

---

## 7. 마이그레이션 가이드

### 7-1. Claude 4.6으로 마이그레이션 시 체크리스트

| 항목 | 기존 (구형 모델) | 신규 (Claude 4.6) |
|---|---|---|
| **출력 명세** | 모호한 지시 가능 | 구체적이고 명시적으로 |
| **도구 촉진 표현** | "CRITICAL: MUST use..." | "Use this when..." |
| **사고 설정** | `budget_tokens` | `adaptive` + `effort` 파라미터 |
| **Prefill 사용** | 마지막 assistant turn prefill | 시스템 프롬프트 지시 또는 Structured Outputs |
| **반복 방지 프롬프트** | 적극적 촉진 필요 | 오히려 축소 필요 (이미 매우 적극적) |

---

### 7-2. Claude Sonnet 4.5 → Sonnet 4.6 마이그레이션

**Effort 파라미터 설정 가이드:**

| 용도 | 권장 Effort | 이유 |
|---|---|---|
| 대부분의 애플리케이션 | `medium` | 품질/속도 균형 |
| 대용량/레이턴시 민감 | `low` | 비용 효율 |
| 자율 멀티스텝 에이전트 | `high` | 복잡한 추론 필요 |
| 컴퓨터 사용 에이전트 | adaptive + `high` | 최고 정확도 |

> Sonnet 4.6은 기본 effort가 `high`다. 명시적으로 설정하지 않으면 레이턴시가 높을 수 있다.

**Extended thinking을 사용하지 않는 경우:**
```python
client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=8192,
    thinking={"type": "disabled"},
    output_config={"effort": "low"},
    messages=[{"role": "user", "content": "..."}],
)
```

**코딩 용도 (에이전트 코딩, 도구 워크플로우):**
```python
client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=16384,
    thinking={"type": "enabled", "budget_tokens": 16384},
    output_config={"effort": "medium"},
    messages=[{"role": "user", "content": "..."}],
)
```

**채팅/비코딩 용도 (콘텐츠 생성, 검색, 분류):**
```python
client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=8192,
    thinking={"type": "enabled", "budget_tokens": 16384},
    output_config={"effort": "low"},
    messages=[{"role": "user", "content": "..."}],
)
```

**Adaptive thinking이 적합한 경우:**
- 자율 멀티스텝 에이전트 (코딩 에이전트, 데이터 분석 파이프라인)
- 컴퓨터 사용 에이전트
- 쉬운 작업과 어려운 작업이 혼재된 이분 워크로드

```python
client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=64000,
    thinking={"type": "adaptive"},
    output_config={"effort": "high"},
    messages=[{"role": "user", "content": "..."}],
)
```

---

## 빠른 참조 카드

### 프롬프트 작성 체크리스트

- [ ] 구체적이고 명시적인 지시 (모호한 표현 제거)
- [ ] 지시 사항에 이유(why) 포함
- [ ] 3~5개의 관련성 있고 다양한 예시 (`<examples>` 태그)
- [ ] XML 태그로 프롬프트 구조화
- [ ] 역할 설정 (시스템 프롬프트)
- [ ] 긴 문서는 프롬프트 상단에 배치
- [ ] 원하는 것을 말하기 (하지 말 것 대신)

### 모델별 핵심 차이점

| | Claude 4.6 (Opus/Sonnet) | 구형 모델 |
|---|---|---|
| **기본 사고** | Adaptive (동적 결정) | Extended with budget_tokens |
| **적극성** | 매우 높음 (촉진 표현 축소 필요) | 보통 (적극적 촉진 필요) |
| **병렬 도구** | 기본적으로 매우 적극적 | 상대적으로 덜 적극적 |
| **Prefill** | 미지원 (4.6부터) | 지원 |
| **자율 에이전트** | 서브에이전트 자연스러운 오케스트레이션 | 명시적 지시 필요 |

---

*최종 업데이트: 2025년 (Claude Opus 4.6, Sonnet 4.6, Haiku 4.5 기준)*
