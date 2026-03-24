# Batch Rules

## 1. 패키지 구조

```
app/batch/src/main/java/com/barlow/
├── app/batch/
│   ├── BatchCoreConfig.java          ← @ComponentScan + @EnableBatchProcessing
│   ├── {bc}/
│   │   ├── job/config/               ← Job, Step @Bean 정의
│   │   ├── job/step/                 ← Tasklet, Reader, Writer 구현
│   │   └── client/                   ← 외부 API Adapter
│   └── common/                       ← AbstractExecutionContextSharingManager, StepLoggingListener
└── infra/storage/batch/
    ├── config/BatchCoreJpaConfig.java ← @EntityScan + @EnableJpaRepositories
    ├── {Bc}BatchJpaRepository.java    ← JPA Repository 인터페이스
    └── {Bc}BatchRepositoryAdapter.java ← Port 구현
```

---

## 2. 레이어 책임

| 클래스 | 어노테이션 | 책임 |
|---|---|---|
| `{Bc}BatchJobConfig` | `@Configuration` | Job/Step @Bean 정의만 |
| `{Bc}Tasklet` | `@Component` + `@StepScope` | Step 비즈니스 로직 |
| `{Bc}Reader` | `@Component` + `@StepScope` | Chunk 아이템 1개씩 읽기 |
| `{Bc}Writer` | `@Component` + `@StepScope` | Chunk 아이템 묶음 쓰기 |
| `{Bc}BatchJobExecutor` | `@Component` | JobLauncher 실행 + Alerter 호출 |
| `{Bc}BatchJpaRepository` | — | JPA 쿼리 전용 |
| `{Bc}BatchRepositoryAdapter` | `@Component` | 도메인 Port → JPA 위임 |

---

## 3. Job 정의 패턴

```java
@Configuration
public class {Bc}BatchJobConfig {

    // JobRepository는 반드시 @Qualifier 사용
    public {Bc}BatchJobConfig(@Qualifier("batchCoreJobRepository") JobRepository jobRepository) { ... }

    @Bean
    public Job {bcJob}(Step step1, Step step2) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step1).next(step2)
                .build();
    }

    @Bean
    @JobScope  // Step은 반드시 @JobScope
    public Step {bcStep}({Bc}Tasklet tasklet, PlatformTransactionManager tm) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(tasklet, tm)
                .listener(stepLoggingListener)
                .build();
    }
}
```

---

## 4. Tasklet vs Chunk 선택 기준

| 조건 | 선택 |
|---|---|
| 단일 처리, API 호출, Dirty Check, 알림 전송 | **Tasklet** |
| 반복적 대량 데이터 처리 (N건 READ → N건 WRITE) | **Chunk** |

---

## 5. 트랜잭션 규칙

- **기본**: `.tasklet(tasklet, transactionManager)` → `PROPAGATION_REQUIRED`
- **알림 전송 Step**: `PROPAGATION_NEVER` 필수 (FCM 등 외부 호출은 트랜잭션 밖에서 실행)
  - *이유: 외부 API 호출(FCM 등)을 DB 트랜잭션 안에 두면 외부 API 응답 대기 시간 동안 DB 커넥션이 점유된다. 또한 외부 호출 성공 후 DB 롤백이 발생해도 이미 발송된 알림을 되돌릴 수 없어 데이터 불일치가 생긴다. 외부 I/O는 반드시 트랜잭션 경계 밖에서 실행해야 한다.*

```java
DefaultTransactionAttribute tx = new DefaultTransactionAttribute();
tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
stepBuilder.tasklet(tasklet, tm).transactionAttribute(tx)
```

---

## 6. Step 간 데이터 공유 패턴

**AbstractExecutionContextSharingManager** 상속 + In-Memory Share Repository 조합.

```
JobListener/InitialTasklet
  → shareRepository.save(hashKey, data)
  → executionContext.put(SHARE_KEY, hashKey)

NextTasklet
  → hashKey = executionContext.get(SHARE_KEY)
  → data = shareRepository.findByKey(hashKey)
```

- 실제 데이터는 Share Repository(HashMap)에 보관
- ExecutionContext에는 Hash 키(String)만 저장

---

## 7. Repository 패턴

**배치 전용 인터페이스를 `app/batch/.../job/` 하위에 정의, 구현은 `infra/storage/batch/`에 위치.**

```java
// app/batch/.../job/ (Port 인터페이스)
public interface {Bc}BatchRepository {
    void updateAllInBatch({Domain} data);
}

// infra/storage/batch/ (JPA Adapter)
@Component
public class {Bc}BatchRepositoryAdapter implements {Bc}BatchRepository {
    private final {Bc}BatchJpaRepository jpaRepository;
    // JPA 위임
}
```

---

## 8. 신규 배치 추가 시 필수 체크리스트

1. **`BatchCoreJpaConfig`**: 신규 `@Entity` 클래스를 `@EntityScan`에 등록
2. **`BatchCoreConfig`**: 신규 패키지가 `@ComponentScan` 범위 안에 있는지 확인
3. **Share Repository**: Step 간 데이터 공유가 필요하면 In-Memory Adapter 구현
4. **알림 전송 Step**: `PROPAGATION_NEVER` 트랜잭션 설정