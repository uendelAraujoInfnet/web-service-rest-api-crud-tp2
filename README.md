# 📦 REST CRUD Demo — Spring Boot 3.5.4 + Java 21

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen)
![Build](https://img.shields.io/badge/build-GitHub%20Actions-lightgrey)
![License](https://img.shields.io/badge/license-MIT-black)

**PT-BR • [EN](#-english) • [FR](#-français)**

---

## 🇧🇷 Português

### 📘 Visão Geral

Este repositório contém uma API REST de exemplo para gestão de **Produtos** com 5 endpoints (listar, buscar, criar, atualizar, excluir), seguindo boas práticas de **padrões REST**, **validação** (Bean Validation), **tratamento global de erros** e **persistência** com JPA/Hibernate. Banco padrão: **H2 em memória** para desenvolvimento/testes.

- **Java:** 21 (LTS)
- **Spring Boot:** 3.5.4
- **Módulos:** Web, Data JPA, Validation, H2
- **Extras:** GlobalExceptionHandler, DTO, exemplos `curl` e testes com MockMvc

---

### 🧱 Arquitetura & Pastas

```
src/main/java/com/example/demo
├─ DemoApplication.java
├─ domain/           # Entidades JPA (Product)
├─ dto/              # DTOs de entrada/saída (ProductDTO)
├─ repository/       # Spring Data JPA (ProductRepository)
├─ service/          # Regras de negócio (ProductService)
├─ web/              # Controllers REST (ProductController)
│  └─ error/         # Exceções e handler global (ApiError, NotFound, Conflict, Handler)
└─ mapper/           # (Opcional) Mapeamento Entity <-> DTO
src/main/resources
├─ application.yml   # Configuração (H2, JPA, porta)
└─ data.sql          # (Opcional) seed de dados
```

---

### 🧩 Requisitos

- JDK 21+
- Maven 3.9+ (ou Maven Wrapper `./mvnw`)
- IDE de sua preferência (IntelliJ/VS Code/Eclipse)

---

### 🚀 Início Rápido

```bash

# 1) Rodar a aplicação
./mvnw spring-boot:run

# 2) Acessos
# API:       http://localhost:8080
# H2 Console http://localhost:8080/h2-console
#   JDBC URL: jdbc:h2:mem:demo
#   User: sa  (sem senha)
```

`src/main/resources/application.yml`
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console
```

> 💡 **Por que Boot 3.5.4?** Linha mais recente (mantida) e compatível com Java 21. Para projetos novos, recomenda-se 3.5.x.

---

### 🧪 Testes

```bash
./mvnw test
```

- Testes usam **SpringBootTest + MockMvc** para validar **códigos de status** e **payloads**.
- Integração fácil com JaCoCo (opcional) para cobertura.

---

### 📚 Modelo (Product) & DTO (ProductDTO)

- **Product**: `id: Long`, `name: String(<=120)`, `price: BigDecimal >= 0`, `stock: Integer >= 0`  
- **ProductDTO**: validações no payload (`@NotBlank`, `@DecimalMin("0.00")`, `@Min(0)`)

> Regra de unicidade: `name` único (conflitos retornam **409 CONFLICT**).

---

### 🔗 Endpoints (API v1)

Base path: `/api/v1/products`

| Método | Caminho                 | Descrição                 | Status de sucesso |
|-------:|-------------------------|---------------------------|-------------------|
| GET    | `/api/v1/products`      | Lista todos               | 200 OK            |
| GET    | `/api/v1/products/{id}` | Busca por ID              | 200 OK (ou 404)   |
| POST   | `/api/v1/products`      | Cria novo produto         | 201 Created       |
| PUT    | `/api/v1/products/{id}` | Atualiza produto existente| 200 OK            |
| DELETE | `/api/v1/products/{id}` | Exclui por ID             | 204 No Content    |

---

### ✅ Convenções de HTTP Status

- **201 Created (+Location)**: criação com sucesso (POST).
- **200 OK**: leitura/atualização com corpo.
- **204 No Content**: exclusão sem corpo.
- **400 Bad Request**: violação de validação (Bean Validation).
- **404 Not Found**: recurso não encontrado.
- **409 Conflict**: conflito de unicidade/integridade.

---

### 🧰 Exemplos `curl`

**Criar (201 + Location)**
```bash
curl -i -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mouse Pro","price":129.90,"stock":15}'
```

**Listar (200)**
```bash
curl -i http://localhost:8080/api/v1/products
```

**Buscar por ID (200/404)**
```bash
curl -i http://localhost:8080/api/v1/products/1
```

**Atualizar (200/404/409)**
```bash
curl -i -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Mouse Pro 2","price":149.90,"stock":10}'
```

**Excluir (204/404)**
```bash
curl -i -X DELETE http://localhost:8080/api/v1/products/1
```

---

### 🚨 Contrato de Erro (JSON)

`ApiError`:
```json
{
  "timestamp": "2025-08-18T14:22:01.234Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validação falhou",
  "path": "/api/v1/products",
  "fieldErrors": {
    "name": "must not be blank",
    "price": "must be greater than or equal to 0.00",
    "stock": "must be greater than or equal to 0"
  }
}
```

---

### 🛡️ Segurança & Actuator (opcional)

- Ao adicionar `spring-boot-starter-actuator`, alguns endpoints são sensíveis. Na linha 3.5.x, `heapdump` é mais restrito por padrão. Configure exposição apenas em ambientes controlados.
- Exemplo de habilitar somente health/info:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

---

### 🧭 Swagger/OpenAPI (opcional)

Adicione no `pom.xml`:
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.6.0</version>
</dependency>
```

Acesse: `http://localhost:8080/swagger-ui.html`

---

### 🐘 Postgres em Produção (opcional)

`application-prod.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo
    username: demo
    password: demo
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
```

Perfil:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

---

### 🐳 Docker (opcional)

`Dockerfile`:
```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Build & run:
```bash
./mvnw -DskipTests clean package
docker build -t demo-api:latest .
docker run --rm -p 8080:8080 demo-api:latest
```

---

### 📝 Convenções Git (sugestão)

- Mensagens no padrão **Conventional Commits**:
  - `feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `chore:`, etc.
- Branches: `feat/*`, `fix/*`, `hotfix/*`, `docs/*`.

---

### 📸 Evidências (para relatório)

Inclua em `docs/`:
- Screenshots do **Spring Initializr** com dependências.
- **H2 Console** mostrando a tabela `products`.
- Capturas do **Postman** ou terminal (`curl`) com respostas 201/200/204/400/404/409.
- Saída dos **testes**.

---

## 🇬🇧 English

### 📘 Overview

This repository provides a sample **REST API** for **Products** with 5 endpoints (list, get, create, update, delete), following **REST** best practices, **Bean Validation**, **global error handling**, and **JPA/Hibernate** persistence. Default DB: **in-memory H2** for dev/test.

- **Java:** 21 (LTS)
- **Spring Boot:** 3.5.4
- **Modules:** Web, Data JPA, Validation, H2
- **Extras:** GlobalExceptionHandler, DTO, `curl` examples, MockMvc tests

---

### 🚀 Quick Start

```bash

./mvnw spring-boot:run

# API:       http://localhost:8080
# H2 Console http://localhost:8080/h2-console
#   JDBC URL: jdbc:h2:mem:demo | user: sa
```

**Key endpoints** (`/api/v1/products`):
- `GET /` — list (200)
- `GET /{id}` — get by id (200/404)
- `POST /` — create (201 + Location)
- `PUT /{id}` — update (200/404/409)
- `DELETE /{id}` — delete (204/404)

**HTTP status mapping:**
- 201 Created, 200 OK, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict.

**Error contract (`ApiError`):**
```json
{
  "timestamp": "2025-08-18T14:22:01.234Z",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found: id=99",
  "path": "/api/v1/products/99",
  "fieldErrors": null
}
```

**Tests:**
```bash
./mvnw test
```

**Optional:** Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`), Dockerfile, Postgres profile for production.

---

## 🇫🇷 Français

### 📘 Aperçu

Ce dépôt propose une **API REST** d’exemple pour gérer des **Produits** avec 5 endpoints (lister, consulter, créer, mettre à jour, supprimer), respectant les bonnes pratiques **REST**, la **validation** (Bean Validation), un **gestionnaire global d’erreurs** et la **persistance** via JPA/Hibernate. Base par défaut : **H2 en mémoire** pour dev/test.

- **Java :** 21 (LTS)
- **Spring Boot :** 3.5.4
- **Modules :** Web, Data JPA, Validation, H2
- **Extras :** GlobalExceptionHandler, DTO, exemples `curl`, tests MockMvc

---

### 🚀 Démarrage rapide

```bash
./mvnw spring-boot:run

# API :        http://localhost:8080
# Console H2 : http://localhost:8080/h2-console
#   JDBC URL : jdbc:h2:mem:demo | user : sa
```

**Principaux endpoints** (`/api/v1/products`) :
- `GET /` — liste (200)
- `GET /{id}` — par id (200/404)
- `POST /` — création (201 + Location)
- `PUT /{id}` — mise à jour (200/404/409)
- `DELETE /{id}` — suppression (204/404)

**Codes HTTP :**
- 201 Created, 200 OK, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict.

**Contrat d’erreur (`ApiError`) :**
```json
{
  "timestamp": "2025-08-18T14:22:01.234Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Échec de validation",
  "path": "/api/v1/products",
  "fieldErrors": {
    "name": "ne doit pas être vide",
    "price": "doit être ≥ 0.00",
    "stock": "doit être ≥ 0"
  }
}
```

**Tests :**
```bash
./mvnw test
```

**Optionnel :** Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`), Dockerfile, profil Postgres pour la production.

---
