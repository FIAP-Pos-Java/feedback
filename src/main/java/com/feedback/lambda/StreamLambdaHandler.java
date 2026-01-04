package com.feedback.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handler para AWS Lambda usando Spring Cloud Function.
 * Este handler processa requisições HTTP via API Gateway e encaminha para a aplicação Spring Boot.
 * 
 * Configure no AWS Lambda:
 * - Handler: com.feedback.lambda.StreamLambdaHandler::handleRequest
 * - Runtime: Java 21
 */
public class StreamLambdaHandler extends FunctionInvoker {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        super.handleRequest(input, output, context);
    }
}

