package com.barlow.batch.core;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@ActiveProfiles("local")
@Tag("context")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class BatchCoreContextTest {
}
