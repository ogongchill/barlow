package com.barlow.app.batch;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BatchApplicationLambdaHandler implements RequestHandler<HttpApiV2ProxyRequest, AwsProxyResponse> {

	private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(BatchCoreApplication.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Unable To Start BatchCoreApplication", e);
		}
	}

	@Override
	public AwsProxyResponse handleRequest(HttpApiV2ProxyRequest awsProxyRequest, Context context) {
		return handler.proxy(awsProxyRequest, context);
	}
}
