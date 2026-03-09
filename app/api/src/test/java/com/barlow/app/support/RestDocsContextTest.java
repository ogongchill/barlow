package com.barlow.app.support;

import static com.barlow.app.support.TestHttpUtils.AUTHENTICATION_TYPE;
import static com.barlow.app.support.TestHttpUtils.AUTHORIZATION;
import static com.barlow.app.support.TestHttpUtils.MANDATORY_DEVICE_HEADERS;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS;
import static com.barlow.app.support.TestHttpUtils.X_CLIENT_OS_VERSION;
import static com.barlow.app.support.TestHttpUtils.X_DEVICE_ID;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.restdocs.snippet.Snippet;

import com.barlow.ContextTest;
import com.barlow.test.api.RestDocUtils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@Tag("restdocs")
@ExtendWith(RestDocumentationExtension.class)
@Import(TestTokenProvider.class)
public abstract class RestDocsContextTest extends ContextTest {

	protected RequestSpecification spec;

	@Autowired
	protected TestTokenProvider testTokenProvider;

	@BeforeEach
	void setUpRestDocs(RestDocumentationContextProvider provider) {
		this.spec = new RequestSpecBuilder()
			.addFilter(RestAssuredRestDocumentation.documentationConfiguration(provider))
			.build();
	}

	protected RestDocumentationFilter document(String identifier, Snippet... snippets) {
		return RestAssuredRestDocumentation.document(identifier,
			RestDocUtils.requestPreprocessor(),
			RestDocUtils.responsePreprocessor(),
			snippets);
	}

	protected static RequestHeadersSnippet authRequestHeaders() {
		return requestHeaders(
			headerWithName(AUTHORIZATION).description("Bearer 액세스 토큰"),
			headerWithName(X_CLIENT_OS).description("클라이언트 OS (ios / android)"),
			headerWithName(X_CLIENT_OS_VERSION).description("클라이언트 OS 버전"),
			headerWithName(X_DEVICE_ID).description("디바이스 ID"));
	}

	protected RequestSpecification givenWithAuth() {
		return RestAssured.given(spec)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + testTokenProvider.getAccessTokenValue())
			.headers(MANDATORY_DEVICE_HEADERS);
	}

	protected RequestSpecification givenWithAuth(String tokenValue) {
		return RestAssured.given(spec)
			.header(AUTHORIZATION, AUTHENTICATION_TYPE + tokenValue)
			.headers(MANDATORY_DEVICE_HEADERS);
	}
}
