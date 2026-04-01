# infra:notification 모듈

FCM(Firebase Cloud Messaging) 기반 푸시 알림 인프라 모듈.  
법안 진행 상황을 iOS/Android 구독자에게 비동기로 발송하고 알림 센터에 기록합니다.

---

## 패키지 구조

```
com.barlow.infra.notification/
├── core/         # 포트 인터페이스 · 알림 요청 · 타입 정의
├── template/     # 알림 메시지 템플릿 (주제별 제목·본문)
├── reader/       # 구독자 정보 조회 (DB → NotificationInfo)
├── repository/   # 구독자 조회·알림 센터 저장 포트
├── worker/       # FCM 비동기 전송 · 재시도 큐
└── config/       # Firebase 설정 · 비동기 Executor
```

---

## 알림 발송 흐름

```
BillNotificationSendService  (NotificationSendPort 구현체)
  │
  ├─ MessageTemplateFactory   → 알림 유형에 맞는 메시지 템플릿 선택
  ├─ NotificationInfoReaderFactory → 구독자 정보 조회 방식 선택
  │    └─ NotificationInfoRepository (DB 조회)
  │
  └─ NotificationSendWorker   → iOS / Android 병렬 비동기 전송
       ├─ IOSNotificationSender   (APNS 설정 포함)
       ├─ AndroidNotificationSender
       ├─ 전송 실패 시 RetryWorker  → FailureMessageRetryQueue (60초 후 1회 재시도)
       └─ NotificationCenterRegistrar → 알림 센터 기록 저장
```

---

## 알림 유형

| 유형 | 설명 |
|------|------|
| `DEFAULT` | 법안 접수·본회의·공포 등 진행 상태 알림 |
| `STANDING_COMMITTEE` | 소관 위원회 기준 알림 |

유형에 따라 `MessageTemplate`(메시지 포맷)과 `NotificationInfoReader`(구독자 조회 방식)가 각각 선택됩니다.

---

## 재시도 정책

- 재시도 대상 오류: `INTERNAL`, `UNAVAILABLE`, `QUOTA_EXCEEDED`
- 대기 시간: 60초 (`DelayQueue` 기반)
- 최대 재시도 횟수: 1회
- 그 외 실패는 로그만 기록

---

## 설정

Firebase 서비스 계정 키 파일 경로를 `notification.yml`로 주입합니다.

```yaml
firebase:
  fcm:
    config:
      path: firebase/barlow-firebase-adminsdk.json
```

비동기 Executor(`notify-async-`) — corePoolSize 10 / maxPoolSize 50 / queueCapacity 10

---

## 의존 모듈

```gradle
implementation 'com.google.firebase:firebase-admin:9.4.1'
compileOnly project(":core:domain")
compileOnly project(":infra:storage")
compileOnly 'org.springframework:spring-context'
compileOnly 'org.springframework.boot:spring-boot-starter-data-jpa'
```
