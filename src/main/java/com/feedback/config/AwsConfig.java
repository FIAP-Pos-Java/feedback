package com.feedback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.ses.SesClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${aws.region:us-east-2}")
    private String awsRegion;

    @Value("${aws.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:}")
    private String secretAccessKey;

    @Value("${aws.sns.endpoint:}")
    private String snsEndpoint;

    @Bean
    public SnsClient snsClient() {
        SnsClientBuilder builder = SnsClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(getCredentialsProvider());

        // Para LocalStack ou outros endpoints customizados
        if (snsEndpoint != null && !snsEndpoint.isEmpty()) {
            builder.endpointOverride(URI.create(snsEndpoint));
        }

        return builder.build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(getCredentialsProvider())
            .build();
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        // Se credenciais estáticas estão configuradas, use-as
        if (accessKeyId != null && !accessKeyId.isEmpty() 
            && secretAccessKey != null && !secretAccessKey.isEmpty()) {
            return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            );
        }
        // Caso contrário, usa a cadeia padrão (IAM role, environment variables, etc.)
        return DefaultCredentialsProvider.create();
    }
}
