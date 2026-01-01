# Sistema de Feedback - AWS Lambda com Quarkus

É um sistema serverless que recebe feedbacks de alunos com notas de 0 a 10.

## Tecnologias

- Java 21
- Quarkus
- AWS Lambda
- DynamoDB (banco de dados)
- CloudWatch (logs)

## Como rodar localmente?

./mvnw quarkus:dev

A aplicação vai rodar em `http://localhost:8080`

ps: Necessário docker ativo

### Testar o endpoint

bash:
curl -X POST http://localhost:8080/avaliacao \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Aula muito boa!",
    "nota": 9
  }'

## Configuração

As configurações ficam no application.properties ou podem ser passadas como variáveis de ambiente:

- `AWS_REGION` - Região AWS (padrão: us-east-1)
- `DYNAMODB_TABLE_NAME` - Nome da tabela (padrão: feedbacks)

## Configuração na AWS

Utilizando DynamoDB, crie a tabela com o nome `feedbacks`
- id (String) - Partition Key
- dataCriacao (String) - Sort Key
- descricao (String)
- nota (Number, 0-10)
- critico (Boolean)

Permissões a lambda acessar o DynamoDB e CloudWatch (IAM)

## Gerando arquivo para colocar na AWS

bash:
./mvnw clean package

### Endpoint POST /avaliacao

**Request:**
```json
{
  "descricao": "Aula excelente!",
  "nota": 9
}
```

**Response esperado:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "mensagem": "Avaliação registrada com sucesso"
}
```

## Dúvidas?

Se tiver algum problema, verifica:
- Se as variáveis de ambiente estão configuradas
- Se as permissões IAM estão corretas
- Se os serviços AWS estão criados (DynamoDB)
- Os logs no CloudWatch para ver erros