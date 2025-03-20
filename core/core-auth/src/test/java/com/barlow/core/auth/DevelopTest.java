package com.barlow.core.auth;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import com.barlow.core.auth.config.TestKeyConfig;

@ActiveProfiles("test")
@Tag("develop")
@SpringBootTest(
	classes = {CoreAuthTestApplication.class, TestKeyConfig.class},
	properties = {"spring.profiles.active=test"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class DevelopTest {
}
