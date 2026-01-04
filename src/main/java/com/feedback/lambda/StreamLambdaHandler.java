package com.feedback.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.feedback.FeedbackApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handler para AWS Lambda usando AWS Serverless Java Container.
 * Este handler processa requisições HTTP via API Gateway e encaminha para a aplicação Spring Boot.
 * Suporta API Gateway REST API (v1) e HTTP API (v2).
 * 
 * Configure no AWS Lambda:
 * - Handler: com.feedback.lambda.StreamLambdaHandler::handleRequest
 * - Runtime: Java 21
 */
public class StreamLambdaHandler implements RequestStreamHandler {
    
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                    .defaultProxy()
                    .asyncInit()
                    .springBootApplication(FeedbackApplication.class)
                    .buildAndInitialize();
        } catch (ContainerInitializationException e) {
            // Se falhar ao inicializar, lançar erro
            e.printStackTrace();
            throw new RuntimeException("Não foi possível inicializar o Spring Boot", e);
        }
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);
    }
}
