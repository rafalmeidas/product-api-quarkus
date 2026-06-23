# Product API

API REST para gerenciamento de produtos desenvolvida com **Quarkus 3.30.6**, **Hibernate ORM Panache** e banco de dados **H2** em memória. Expõe um CRUD completo documentado via **Swagger UI / OpenAPI 3**.

---

## Sumário

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Links de acesso](#links-de-acesso)
- [Exemplos de requisição](#exemplos-de-requisição)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Build para produção](#build-para-produção)

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Quarkus | 3.30.6 |
| Hibernate ORM Panache | via Quarkus BOM |
| SmallRye OpenAPI (Swagger) | via Quarkus BOM |
| H2 (banco em memória) | via Quarkus BOM |
| Maven | 3.9+ |

---

## Pré-requisitos

- **JDK 21+** instalado e configurado no `PATH`
- **Maven 3.9+** (ou usar o wrapper `./mvnw` incluído no projeto)

---

## Como executar

### Modo desenvolvimento (hot reload ativo)

```bash
./mvnw quarkus:dev
```

> No Windows use `mvnw.cmd quarkus:dev`

A aplicação sobe em `http://localhost:8080` com **live reload** — qualquer alteração no código é refletida automaticamente sem reiniciar.

### Modo produção (JAR)

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Endpoints

Base URL: `http://localhost:8080`

| Método | Rota | Descrição | Status de sucesso |
|--------|------|-----------|-------------------|
| `GET` | `/products` | Lista todos os produtos | `200 OK` |
| `GET` | `/products/{id}` | Busca um produto pelo ID | `200 OK` |
| `POST` | `/products` | Cria um novo produto | `201 Created` |
| `PUT` | `/products/{id}` | Atualiza um produto existente | `200 OK` |
| `DELETE` | `/products/{id}` | Remove um produto | `204 No Content` |

### Produto — corpo JSON

```json
{
  "name": "Notebook",
  "description": "Notebook Dell i7 16GB",
  "price": 4999.99,
  "active": true
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `name` | `string` | Sim | Nome do produto |
| `description` | `string` | Não | Descrição detalhada |
| `price` | `number` | Sim | Preço (decimal) |
| `active` | `boolean` | Não | Ativo? (padrão: `true`) |

---

## Links de acesso

> Disponíveis com a aplicação rodando localmente.

| Recurso | URL |
|---------|-----|
| API (base) | http://localhost:8080 |
| Swagger UI | http://localhost:8080/q/swagger-ui |
| OpenAPI spec (JSON) | http://localhost:8080/q/openapi |
| H2 Console | http://localhost:8080/q/h2 |
| Dev UI (modo dev) | http://localhost:8080/q/dev |

### Configuração do H2 Console

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:produtos` |
| Username | `sa` |
| Password | `sa` |

---

## Exemplos de requisição

### Criar produto

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Teclado","description":"Teclado mecânico RGB","price":349.00,"active":true}'
```

### Listar todos

```bash
curl http://localhost:8080/products
```

### Buscar por ID

```bash
curl http://localhost:8080/products/1
```

### Atualizar produto

```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Teclado Pro","description":"Teclado mecânico RGB sem fio","price":499.00,"active":true}'
```

### Remover produto

```bash
curl -X DELETE http://localhost:8080/products/1
```

---

## Estrutura do projeto

```
product-api/
├── src/
│   ├── main/
│   │   ├── docker/
│   │   │   ├── Dockerfile.jvm              # Build JVM
│   │   │   ├── Dockerfile.native           # Build native
│   │   │   ├── Dockerfile.native-micro     # Build native micro
│   │   │   └── Dockerfile.legacy-jar       # Build legacy JAR
│   │   ├── java/com/pdi/
│   │   │   ├── entity/
│   │   │   │   └── Product.java            # Entidade JPA
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java  # Repositório Panache
│   │   │   └── resource/
│   │   │       └── ProductResource.java    # Endpoints REST — CRUD completo
│   │   └── resources/
│   │       ├── application.properties      # Configurações da aplicação
│   │       └── import.sql                  # Dados iniciais (dev/test)
│   └── test/
│       └── java/com/pdi/
│           ├── ProductResourceTest.java     # Testes CRUD com @QuarkusTest
│           ├── GreetingResourceTest.java    # Placeholder para modo packaged
│           └── GreetingResourceIT.java      # Testes de integração (@QuarkusIntegrationTest)
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── .dockerignore
├── .gitignore
├── mvnw / mvnw.cmd                          # Maven wrapper
└── pom.xml
```

---

## Build para produção

### JAR padrão

```bash
./mvnw package -DskipTests
java -jar target/quarkus-app/quarkus-run.jar
```

### Über-JAR (tudo em um arquivo)

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar -DskipTests
java -jar target/product-api-1.0.0-SNAPSHOT-runner.jar
```

### Native image (requer GraalVM)

```bash
./mvnw package -Pnative -DskipTests
./target/product-api-1.0.0-SNAPSHOT-runner
```

> O Swagger UI é desabilitado automaticamente em produção. Para mantê-lo ativo, adicione em `application.properties`:
> ```properties
> quarkus.swagger-ui.always-include=true
> ```

---

## Guias relacionados

- [Quarkus REST](https://quarkus.io/guides/rest)
- [Hibernate ORM com Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [SmallRye OpenAPI / Swagger UI](https://quarkus.io/guides/openapi-swaggerui)
- [JDBC Driver H2](https://quarkus.io/guides/datasource)
