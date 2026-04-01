# infra:clients:knal-api 모듈

국회 입법 정보 공공 API(KNAL)를 Spring Cloud OpenFeign으로 감싼 클라이언트 모듈.  
포트-어댑터 패턴으로 비즈니스 로직과 외부 API 의존성을 분리합니다.

---

## 패키지 구조

```
com.barlow.infra.knal/
├── FeignConfig.java              # @EnableFeignClients
├── opencongress/api/             # 국회 OpenCongress API (선행공시)
│   ├── NationalAssemblyLegislationOpenCongressApi  # Feign Client
│   ├── OpenCongressApiPort       # 포트 인터페이스
│   ├── OpenCongressApiAdapter    # 어댑터 구현체
│   ├── OpenCongressConfig        # 인터셉터·재시도·디코더 설정
│   └── common/ · error/          # 공통 DTO · 에러 코드
└── opendata/api/                 # 국회 OpenData API (의안 정보)
    ├── NationalAssemblyLegislationOpenDataApi      # Feign Client
    ├── OpenDataApiPort           # 포트 인터페이스
    ├── OpenDataApiAdapter        # 어댑터 구현체
    ├── OpenDataConfig            # 인터셉터·재시도·인코더·디코더 설정
    └── code/ · request/ · response/  # 코드 상수 · 요청/응답 DTO
```

---

## 외부 API

| API | 용도 | 응답 형식 |
|-----|------|---------|
| **OpenCongress API** | 의안 선행공시(예고) 정보 조회 | JSON |
| **OpenData API** | 의안 목록·심사·접수·공포 등 상세 정보 조회 | XML |

### 공개된 포트 메서드

**OpenCongressApiPort**
- `getPreAnnouncement()` — 의안 선행공시 목록

**OpenDataApiPort**
- `getBillInfoList()` — 의안 목록 검색
- `getBillPetitionMemberList()` — 의안/청원 접수 국회의원 목록
- `getBillPreliminaryExaminationInfo()` — 의안 예비 심사 정보

> 어댑터에는 추가 엔드포인트가 구현되어 있으나 포트에 노출하지 않고 내부에서만 사용합니다.

---

## 설정 (client-knal-api.yml)

```yaml
knal:
  open-data:
    api:
      url: https://apis.data.go.kr/9710000/BillInfoService2/
      service-key: ${KNAL_OPEN_DATA_API_SERVICE_KEY}
  open-congress:
    api:
      url: https://open.assembly.go.kr/portal/openapi/
      service-key: ${KNAL_OPEN_CONGRESS_API_SERVICE_KEY}

spring.cloud.openfeign.client.config:
  connectTimeout: 3000    # 3초
  readTimeout: 10000      # 10초
```

서비스 키는 각 API의 인증 인터셉터를 통해 모든 요청에 자동으로 쿼리 파라미터로 첨부됩니다.

---

## 에러 처리

응답 디코더가 성공/에러 여부를 판별해 에러 응답 시 전용 예외를 발생시킵니다.

| 예외 | 발생 조건 |
|------|---------|
| `OpenCongressException` | OpenCongress API 에러 응답 (JSON) |
| `OpenDataException` | OpenData API 에러 응답 (XML) |

---

## 의존 모듈

```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
implementation 'io.github.openfeign:feign-hc5'
implementation 'io.github.openfeign:feign-micrometer'
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'
```
