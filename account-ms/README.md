# Aula Account Microservice CESMAC (account-ms)

Microsserviço RESTful construído com **Spring Boot 4.0.2** e **Java 17** para gerenciamento de contas de clientes. Este projeto demonstra uma arquitetura em camadas limpa, comumente utilizada em aplicações Java corporativas.


### Atenção (Alunos)

- Deverá ser substituido o h2 utilizado em sala de aula por outro banco relacional (Postgres; Oracle; etc);
- Não é obrigatório seguir os packages desta aplicação no seu projeto. Pode seguir o padrão arquitetural da sua empresa ou do seu estudo;
- Tudo que estiver no seu projeto, será necessário explicação na apresentação
- É permitido o uso de Lombok. Este projeto já utiliza Lombok nesta branch (`sample-lombok`).
## Stack Tecnológica

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem de programação |
| Spring Boot 4.0.2 | Framework da aplicação |
| Spring Web MVC | Camada de API REST |
| Spring Data JPA | Acesso a dados / ORM |
| Bean Validation | Validação de requisições |
| Lombok | Redução de boilerplate (getters, setters, construtores) |
| H2 Database | Banco de dados relacional em memória |
| Gradle 9.3 | Ferramenta de build |

## Arquitetura

Este projeto segue o padrão de **Arquitetura em Camadas (Layered Architecture)**, onde cada camada possui uma única responsabilidade e se comunica apenas com as camadas adjacentes.

```
┌─────────────────────────────────────────────────────────┐
│                     Cliente (HTTP)                       │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   Camada Controller                      │
│              (CustomerController.java)                   │
│  Recebe requisições HTTP, delega para o Service e        │
│  retorna ResponseEntity com os status codes adequados.   │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                    Camada Service                        │
│               (CustomerService.java)                     │
│  Contém a lógica de negócio. Orquestra chamadas entre    │
│  as camadas Repository e Mapper.                         │
└──────────────┬───────────────────────────┬──────────────┘
               │                           │
               ▼                           ▼
┌──────────────────────────┐ ┌────────────────────────────┐
│   Camada Repository      │ │      Camada Mapper          │
│ (CustomerRepository.java)│ │  (CustomerMapper.java)      │
│ Estende JpaRepository,   │ │  Converte entre objetos     │
│ fornece operações CRUD   │ │  Entity e DTO.              │
│ no banco de dados.       │ │                              │
└──────────────┬───────────┘ └────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────────────────┐
│               Banco de Dados H2 (em memória)            │
└─────────────────────────────────────────────────────────┘
```

## Estrutura de Pacotes

```
com.example.account_ms
├── AccountMsApplication.java          # Ponto de entrada do Spring Boot
├── controller/
│   └── CustomerController.java        # Endpoints REST (@RestController, @RequiredArgsConstructor)
├── service/
│   └── CustomerService.java           # Lógica de negócio (@Service, @RequiredArgsConstructor)
├── repository/
│   └── CustomerRepository.java        # Acesso a dados (@Repository)
├── model/
│   └── Customer.java                  # Entidade JPA (@Entity, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor)
├── dto/
│   ├── input/
│   │   ├── CreateCustomerRequestDto.java   # Corpo da requisição POST
│   │   ├── UpdateCustomerRequestDto.java   # Corpo da requisição PUT
│   │   └── PatchCustomerRequestDto.java    # Corpo da requisição PATCH
│   └── output/
│       ├── CustomerResponseDto.java        # Corpo da resposta da API
│       └── ErrorResponseDto.java           # Corpo da resposta de erro
├── mappers/
│   └── CustomerMapper.java            # Conversão Entity <-> DTO (@Component)
└── exceptions/
    ├── ResourceNotFoundException.java  # Exceção customizada para 404
    └── handler/
        └── GlobalExceptionHandler.java # Tratamento centralizado de erros (@RestControllerAdvice)
```

## Conceitos-Chave Explicados

### 1. Camada Controller

O `CustomerController` é anotado com `@RestController` e mapeia requisições HTTP para métodos Java. Ele **não contém lógica de negócio** -- apenas recebe as requisições, delega para o service e retorna respostas com os status codes HTTP apropriados. A anotação `@RequiredArgsConstructor` do Lombok gera automaticamente o construtor para injeção das dependências `final`.

- `POST /api/customers` -- cria um cliente (retorna `204 No Content`)
- `GET /api/customers` -- lista todos os clientes
- `GET /api/customers/{uuid}` -- busca um cliente pelo ID
- `PUT /api/customers/{uuid}` -- atualização completa de um cliente
- `PATCH /api/customers/{uuid}` -- atualização parcial de um cliente
- `DELETE /api/customers/{uuid}` -- remove um cliente (retorna `204 No Content`)

### 2. Camada Service

O `CustomerService` é anotado com `@Service` e contém toda a **lógica de negócio**. Ele utiliza o `CustomerRepository` para operações no banco de dados e o `CustomerMapper` para converter entre entidades e DTOs. A camada de service é onde você valida regras de negócio e orquestra operações. Assim como no controller, utiliza `@RequiredArgsConstructor` para injeção de dependências.

### 3. Camada Repository

O `CustomerRepository` estende `JpaRepository<Customer, UUID>`, o que fornece um conjunto completo de operações CRUD gratuitamente (ex.: `save`, `findById`, `findAll`, `deleteById`, `existsById`). Nenhum código de implementação é necessário -- o Spring Data JPA gera tudo em tempo de execução.

### 4. Model (Entidade)

A entidade `Customer` é uma classe anotada com JPA, mapeada para uma tabela no banco de dados. Ela utiliza `UUID` como chave primária com `GenerationType.UUID` para geração automática.

Campos: `id` (UUID), `name`, `email`, `password`.

As anotações do Lombok eliminam todo o boilerplate da entidade:

| Anotação | Substitui |
|---|---|
| `@Getter` | Todos os métodos `get*()` |
| `@Setter` | Todos os métodos `set*()` |
| `@NoArgsConstructor` | Construtor vazio (exigido pelo JPA) |
| `@AllArgsConstructor` | Construtor com todos os campos |

> **Nota:** Para entidades JPA, evita-se `@Data` pois ele gera `equals()`/`hashCode()` baseados em todos os campos, o que pode causar problemas com lazy loading e proxies do Hibernate.

### 5. DTOs (Data Transfer Objects)

Os DTOs separam o contrato da API do modelo interno. Este projeto utiliza **Java Records** para os DTOs, que são portadores de dados imutáveis.

- **DTOs de entrada** definem o que o cliente envia (com anotações de validação como `@NotNull`, `@NotBlank`, `@Email`).
- **DTOs de saída** definem o que o cliente recebe (observe que o `password` nunca é exposto nas respostas).

### 6. Mapper

O `CustomerMapper` converte entre `Customer` (entidade) e DTOs. Isso mantém a lógica de conversão centralizada e fora da camada de service. Para projetos maiores, considere utilizar o [MapStruct](https://mapstruct.org/) para gerar esses mapeamentos automaticamente.

### 7. Tratamento Global de Exceções

O `GlobalExceptionHandler` é anotado com `@RestControllerAdvice` e intercepta exceções lançadas em qualquer parte da aplicação. Ele as converte em respostas JSON estruturadas de erro (`ErrorResponseDto`) contendo timestamp, código de status, mensagem de erro e path da requisição.

| Exceção | Status HTTP | Quando |
|---|---|---|
| `ResourceNotFoundException` | `404 Not Found` | O ID do cliente não existe |
| `Exception` (genérica) | `500 Internal Server Error` | Qualquer exceção não tratada |

## Referência da API REST

### Criar Cliente

```
POST /api/customers
Content-Type: application/json

{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "password": "123456"
}

Resposta: 204 No Content
```

### Listar Todos os Clientes

```
GET /api/customers

Resposta: 200 OK
[
  {
    "id": "a1b2c3d4-...",
    "name": "Maria Silva",
    "email": "maria@email.com"
  }
]
```

### Buscar Cliente por ID

```
GET /api/customers/{uuid}

Resposta: 200 OK
{
  "id": "a1b2c3d4-...",
  "name": "Maria Silva",
  "email": "maria@email.com"
}
```

### Atualização Completa (PUT)

Substitui `name` e `email`. Todos os campos são obrigatórios.

```
PUT /api/customers/{uuid}
Content-Type: application/json

{
  "name": "Maria Santos",
  "email": "maria.santos@email.com"
}

Resposta: 200 OK
```

### Atualização Parcial (PATCH)

Atualiza apenas os campos fornecidos. Campos omitidos permanecem inalterados.

```
PATCH /api/customers/{uuid}
Content-Type: application/json

{
  "email": "novo@email.com"
}

Resposta: 200 OK
```

### Remover Cliente

```
DELETE /api/customers/{uuid}

Resposta: 204 No Content
```

### Exemplo de Resposta de Erro

```
GET /api/customers/uuid-invalido

Resposta: 404 Not Found
{
  "timestamp": "2026-02-19T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "ID invalido!",
  "path": "/api/customers/uuid-invalido"
}
```

## Como Executar

### Pré-requisitos

- Java 17+

### Executar a aplicação

```bash
./gradlew bootRun
```

A aplicação inicia em **http://localhost:8080**.

### Acessar o Console H2

O console do banco de dados H2 em memória está disponível em **http://localhost:8080/h2-console** com as seguintes configurações:

| Configuração | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa` |
| Password | *(vazio)* |

## Diagrama de Fluxo de uma Requisição

Abaixo está o fluxo passo a passo de uma requisição `GET /api/customers/{uuid}`:

```
1. Cliente envia GET /api/customers/abc-123
                    │
2. CustomerController.findById(uuid) recebe a requisição
                    │
3. CustomerService.findById(uuid) é chamado
                    │
4. CustomerRepository.findById(uuid) consulta o banco H2
                    │
5a. Encontrado ──► CustomerMapper converte Entity → CustomerResponseDto
                   │
                   └──► Controller retorna ResponseEntity<200, dto>
                         │
5b. Não encontrado ──► lança ResourceNotFoundException
                         │
                         └──► GlobalExceptionHandler captura a exceção
                              e retorna ResponseEntity<404, ErrorResponseDto>
```
