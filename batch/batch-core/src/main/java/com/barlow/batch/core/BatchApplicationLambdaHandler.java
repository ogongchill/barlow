package com.barlow.batch.core;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BatchApplicationLambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

	private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(BatchCoreApplication.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Unable To Start BatchCoreApplication", e);
		}
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
		return handler.proxy(awsProxyRequest, context);
	}
}
