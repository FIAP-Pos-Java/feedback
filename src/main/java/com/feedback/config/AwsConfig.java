package com.feedback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${aws.region:us-east-2}")
    private String awsRegion;

    @Value("${aws.sns.endpoint:}")
    private String snsEndpoint;

    @Bean
    public SnsClient snsClient() {
        var builder = SnsClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(DefaultCredentialsProvider.create());

        if (snsEndpoint != null && !snsEndpoint.isEmpty()) {
            builder.endpointOverride(URI.create(snsEndpoint));
        }

        return builder.build();
    }
}
