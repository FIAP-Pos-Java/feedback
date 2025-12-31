package com.feedback.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ApplicationScoped
public class AwsConfig {

    @ConfigProperty(name = "aws.region", defaultValue = "us-east-1")
    String awsRegion;

    @Produces
    @ApplicationScoped
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();

        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }
}
