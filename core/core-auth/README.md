# core-notification 모듈

## 개요

core-notification 모듈은 Barlow 시스템에서 사용자에게 법안 관련 알림을 전송하는 핵심 기능을 담당합니다. 이 모듈은 iOS와 Android 플랫폼 모두에 푸시 알림을 전송하고, 알림 정보를
관리하며, 알림 센터에 알림 항목을 등록하는 기능을 제공합니다.

## 주요 구성 요소

### 1. 알림 전송 시스템

**NotificationSendWorker** <br>
알림 전송의 핵심 컴포넌트로, 비동기적으로 iOS와 Android 기기에 푸시 알림을 전송합니다. NotificationSendWorker.java:18-22

#### 주요 기능:

- 비동기 실행기(Executor)를 사용한 병렬 알림 전송
- 플랫폼별(iOS/Android) 알림 전송 처리 NotificationSendWorker.java:35-46
- 실패한 알림에 대한 자동 재시도 메커니즘 NotificationSendWorker.java:66-70

### 2. 알림 정보 관리

**NotificationInfo** <br>
알림 데이터를 관리하는 클래스로, 주제(Topic)와 구독자(Subscriber) 정보를 매핑하여 저장합니다. NotificationInfo.java:10-16

#### 주요 기능:

- 주제별 알림 수신자 관리
- 주제별 법안 총 개수 할당 NotificationInfo.java:18-22
- 대표 법안 이름 및 총 개수 할당 NotificationInfo.java:24-35

### 3. 알림 센터 등록 시스템

**NotificationCenterRegistrar** <br>
사용자에게 전송된 알림을 알림 센터에 등록하는 역할을 담당합니다. NotificationCenterRegistrar.java:7-14

#### 주요 기능:

- 알림 정보를 알림 센터 항목으로 변환
- 알림 센터에 알림 항목 일괄 등록 NotificationCenterRegistrar.java:16-30

### 4. 알림 센터 저장소

**NotificationCenterRepositoryAdapter** <br>
알림 센터 항목을 데이터베이스에 저장하는 역할을 담당합니다. NotificationCenterRepositoryAdapter.java:20-29

#### 주요 기능:

- 알림 센터 항목 일괄 등록 NotificationCenterRepositoryAdapter.java:31-38

## 기술적 특징

- 비동기 처리: CompletableFuture와 전용 Executor를 사용하여 알림 전송을 비동기적으로 처리함으로써 시스템 성능에 미치는 영향을 최소화합니다.
- 재시도 메커니즘: 알림 전송 실패 시 RetryWorker를 통해 자동으로 재시도합니다.
- 멀티 플랫폼 지원: iOS와 Android 플랫폼에 대한 알림 전송을 모두 지원합니다.
- 배치 시스템 통합: 배치 시스템과 통합되어 법안 업데이트를 감지하고 알림을 생성합니다. build.gradle:4-7

## 데이터 모델

알림 시스템은 다음과 같은 주요 데이터 엔티티를 사용합니다:

1. **NotificationConfigJpaEntity**: 사용자의 알림 설정을 저장 NotificationConfigJpaRepository.java:12-16
2. **NotificationCenterItemJpaEntity**: 사용자에게 전송된 알림 항목을 저장

## 사용자 알림 설정 관리

사용자가 계정을 생성할 때 기본 알림 설정이 자동으로 활성화됩니다: NotificationSettingActivator.java:43-48

## 통합 및 연동

`core-notification` 모듈은 다음과 같은 다른 모듈과 통합되어 작동합니다:

1. 배치 시스템: 법안 업데이트를 감지하고 알림을 생성하는 배치 작업과 연동
2. 사용자 계정 시스템: 사용자 생성 시 기본 알림 설정 활성화
3. 홈 화면 시스템: 사용자의 알림 상태를 홈 화면에 표시

## 요약

`core-notification` 모듈은 Barlow 시스템에서 사용자에게 법안 업데이트 알림을 전송하는 핵심 기능을 담당합니다. 이 모듈은 알림 설정 관리, 알림 정보 구성, 알림 센터 등록, 그리고 실제 알림 전송
과정을 처리하며, 비동기 처리와 재시도 메커니즘을 통해 안정적인 알림 서비스를 제공합니다.