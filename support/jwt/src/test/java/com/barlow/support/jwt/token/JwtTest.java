package com.barlow.support.jwt.token;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestKeyConfig.class)
public abstract class JwtTest {
}
