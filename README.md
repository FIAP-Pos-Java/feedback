Sistema de Feedback

Sistema serverless que recebe feedbacks de alunos com notas de 0 a 10.

## Tecnologias usadas
Java 21, Quarkus, AWS Lambda, DynamoDB e CloudWatch.

## Como rodar localmente
./mvnw quarkus:dev

Vai rodar em http://localhost:8080
Precisa do docker ligado.

## Testar o endpoint
curl -X POST http://localhost:8080/avaliacao \
  -H "Content-Type: application/json" \
  -d '{"descricao": "Aula muito boa!", "nota": 9}'

## Configuração
As configs ficam no application.properties ou variáveis de ambiente:
- AWS_REGION (padrão: us-east-1)
- DYNAMODB_TABLE_NAME (padrão: feedbacks)

## Na AWS
Criar tabela DynamoDB chamada "feedbacks" com campos:
- id (String) - Partition Key
- dataCriacao (String) - Sort Key
- descricao (String)
- nota (Number, 0-10)
- critico (Boolean)

Dar permissões IAM para a lambda acessar DynamoDB e CloudWatch.

## Gerar arquivo para AWS
./mvnw clean package

## Endpoint POST /avaliacao
Envia:
{"descricao": "Aula excelente!", "nota": 9}

Recebe:
{"id": "uuid-aqui", "mensagem": "Avaliação registrada com sucesso"}

## Problemas?
Verifica se:
- Variáveis de ambiente estão certas
- Permissões IAM corretas
- Serviços AWS criados (DynamoDB)
- Logs no CloudWatch