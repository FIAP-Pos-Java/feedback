# Dockerfile para Spring Boot Application
# Para teste local (não necessário para AWS Lambda)

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/feedback-1.0.0-SNAPSHOT.jar app.jar

# Expor porta 8080
EXPOSE 8080

# Variáveis de ambiente padrão (sobrescrever em runtime)
ENV DATABASE_URL=jdbc:postgresql://localhost:5432/feedback
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=postgres
ENV SNS_TOPIC_ARN=
ENV AWS_REGION=us-east-2

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

