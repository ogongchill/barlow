package com.barlow.test.api;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

public class RestDocUtils {

	public static OperationRequestPreprocessor requestPreprocessor() {
		return Preprocessors.preprocessRequest(
			Preprocessors.modifyUris().scheme("http").host("barlow.com").removePort(),
			Preprocessors.prettyPrint());
	}

	public static OperationResponsePreprocessor responsePreprocessor() {
		return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
	}
}
