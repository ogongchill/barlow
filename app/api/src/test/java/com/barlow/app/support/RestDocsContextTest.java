package com.barlow.app.support;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;

import com.barlow.ContextTest;

@Tag("restdocs")
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsContextTest extends ContextTest {

	protected RequestSpecification spec;

	@BeforeEach
	void setUpRestDocs(RestDocumentationContextProvider provider) {
		this.spec = new RequestSpecBuilder()
			.addFilter(RestAssuredRestDocumentation.documentationConfiguration(provider))
			.build();
	}
}
