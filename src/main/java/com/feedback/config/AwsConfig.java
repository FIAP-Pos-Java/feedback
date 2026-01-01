package com.feedback.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@ApplicationScoped
public class AwsConfig {

    @ConfigProperty(name = "aws.region", defaultValue = "us-east-1")
    String awsRegion;

    @Produces
    @ApplicationScoped
    public SnsClient snsClient() {
        return SnsClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();
    }
}
