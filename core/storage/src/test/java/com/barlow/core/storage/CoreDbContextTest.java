package com.barlow.core.storage;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@ActiveProfiles("test")
@Tag("context")
@SpringBootTest(
	classes = CoreDbTestApplication.class,
	properties = {"spring.profiles.active=test"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class CoreDbContextTest {
}
