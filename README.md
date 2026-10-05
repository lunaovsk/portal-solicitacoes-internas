# API - Documentação

API REST para o Portal de Solicitações Internas, desenvolvida como backend do mini-projeto Full Stack. Ela fornece autenticação, controle de acesso, cadastro e gerenciamento de solicitações, filtros e indicadores para dashboards. 

*obs: O frontend não faz parte deste repositório.*
repository(Frontend): 

## Requisitos do projeto

- Login com usuário e senha, autenticação stateless por JWT e logout com revogação do token e persistência do token revolgado no banco.
- Cadastro de solicitações com título, descrição e categoria e novas solicitações começam com status `OPEN` e data já preenchida.
- Edição e exclusão de solicitações abertas pelo criador da solicitação.
- Listagem das solicitações do solicitante ou listagem global para atendentes.
- Consulta detalhada por ID para atendentes.
- Filtros por período, categoria, status e texto no título.
- Alteração do status conforme o usuario autenticado.
- Visualização do dashboard conforme usuario autenticado

## Tecnologias

| Item | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Build | Maven Wrapper |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | MySQL 8 |
| Migrações | Flyway |
| Segurança | Spring Security, BCrypt e JWT (HS256) |
| Documentação da API | SpringDoc OpenAPI / Swagger UI |
| Integração contínua | GitHub Actions |

## Pré-requisitos

- JDK 21.
- MySQL 8 acessível pela máquina.
- Git, para clonar o repositório.

O Maven Wrapper (`mvnw` / `mvnw.cmd`) está incluído; não é necessário instalar Maven separadamente.

## Configuração e execução local

1. Clone o repositório:

   ```bash
   git clone https://github.com/lunaovsk/portal-solicitacoes-internas.git
   cd portal-solicitacoes-internas
   ```

2. Prepare o MySQL. O perfil `dev` está configurado para conectar a `localhost:3306`, usando o banco `db_portal_solicitacoes` e o usuário definido em `src/main/resources/application-dev.properties`. A URL inclui `createDatabaseIfNotExist=true`, então a conta do banco precisa ter permissão para criar o banco. Ajuste as configurações do perfil `dev` se a sua instalação do MySQL usar outro host, usuário ou senha.

3. Crie um arquivo `.env` na raiz do projeto para fornecer o segredo JWT e as credenciais das contas iniciais. O `.env` é carregado pela aplicação e está ignorado pelo Git. Use este formato de propriedades:

   ```properties
   API_SECURITY_TOKEN_SECRET=substitua-por-um-segredo-aleatorio-com-pelo-menos-32-bytes
   ATTEN_BOOTSTRAP_EMAIL=atendente@exemplo.local
   ATTEN_BOOTSTRAP_PASSWORD=defina-uma-senha-forte
   REQ_BOOTSTRAP_EMAIL=solicitante@exemplo.local
   REQ_BOOTSTRAP_PASSWORD=defina-outra-senha-forte
   ```

   Os valores acima são exemplos: substitua-os antes de iniciar. O segredo JWT precisa ter pelo menos 32 bytes. Não use esses valores de exemplo em ambientes compartilhados ou de produção e nunca versione o arquivo `.env`.

   > **Atenção:** o `.env-example` incluído no repositório é apenas uma referência inicial.

4. Inicie a API:

   **Windows (PowerShell)**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **Linux/macOS**

   ```bash
   ./mvnw spring-boot:run
   ```

   A API por padrão fica disponível em `http://localhost:8080`. Na inicialização, o Flyway aplica as migrações pendentes, e duas contas são provisionadas caso os usernames ainda não existam: uma `ATTENDANT` e uma `REQUESTER`.

### Docker: instalação e build da imagem

#### Instalar o Docker no Windows

1. Confira os [requisitos do Docker Desktop para Windows](https://docs.docker.com/desktop/setup/install/windows-install/), incluindo a disponibilidade do WSL 2.
2. Baixe e instale o **Docker Desktop para Windows** pelo site oficial do [Docker](https://www.docker.com/products/docker-desktop/). Durante a instalação, selecione o backend WSL 2 quando essa opção for apresentada.
3. Se o instalador solicitar, reinicie o computador. Abra o Docker Desktop e aguarde até indicar que o mecanismo Docker está em execução.
4. No PowerShell, confirme a instalação:

   ```powershell
   docker --version
   docker compose version
   ```

#### Criar a imagem da API

O Dockerfile usa um build em múltiplas etapas: primeiro compila e empacota a API com Maven e Java 21; depois copia o JAR para uma imagem menor com Java 21 JRE, que é usada para executar a aplicação.

1. Abra o PowerShell na pasta raiz do repositório, onde está o `Dockerfile`.
2. Execute o comando abaixo para construir a imagem para Linux ARM64 e identificá-la com o nome `lunaosvki/portal-solicitacao` e a tag `1.0`:

   ```powershell
   docker build --platform linux/arm64 -t lunaosvki/portal-solicitacao:1.0 .
   ```

   O ponto final indica que a pasta atual é o contexto do build.
3. Ao concluir, confira se a imagem foi criada:

   ```powershell
   docker image ls lunaosvki/portal-solicitacao
   ```

### Migrações do banco

As migrações ficam em `src/main/resources/db/migration` e são aplicadas automaticamente pelo Flyway:

- `V1__create_tables.sql`: cria as tabelas de usuários, solicitações e tokens revogados.
- `V2__alter_table_user.sql`: atualiza os perfis para `REQUESTER` e `ATTENDANT`.

Não é necessário executar os scripts manualmente.

## Autenticação e perfis

Faça login em `POST /api/v1/auth/login` com as credenciais configuradas para uma das contas iniciais:

```json
{
  "username": "solicitante@exemplo.local",
  "password": "a-senha-configurada"
}
```

A resposta contém o JWT:

```json
{
  "tokenJwt": "eyJ..."
}
```

Envie esse token nas rotas protegidas usando:

```http
Authorization: Bearer <tokenJwt>
```

O token expira após uma hora. O logout em `POST /api/v1/auth/logout` registra o token como revogado e responde `204 No Content`.

| Perfil | Permissões principais |
| --- | --- |
| `REQUESTER` | Criar solicitações; <br/>listar as próprias; <br/>editar ou excluir as próprias enquanto estiverem abertas; <br/>consultar o próprio dashboard. |
| `ATTENDANT` | Listar todas as solicitações, consultar detalhes por ID, alterar status e consultar o dashboard global. |

O cadastro público (caso utilize as configurações oferecidas):
- Atendente: email: `atendente@portalsolicitacao.com` senha: `Senha123@`
- Solicitante: email: `request@portalsolicitacao.com` senha: `Senha123@`
## Endpoints

Todas as rotas usam o prefixo `/api/v1`. Exceto login e documentação Swagger, exigem JWT. A autorização por perfil é aplicada no backend.

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Público | Autentica usuário e senha e retorna um JWT. |
| `POST` | `/auth/logout` | Autenticado | Revoga o token atual; retorna `204`. |
| `POST` | `/request` | `REQUESTER` | Cria uma solicitação aberta. |
| `GET` | `/request/filter` | `REQUESTER` | Lista as solicitações do usuário autenticado, com filtros opcionais. |
| `GET` | `/request/all` | `ATTENDANT` | Lista todas as solicitações, com filtros opcionais. |
| `GET` | `/request/{id}` | `ATTENDANT` | Consulta os detalhes de uma solicitação pelo ID. |
| `PUT` | `/request/{id}/update` | `REQUESTER` | Edita uma solicitação própria ainda aberta. |
| `PATCH` | `/request/{id}/status` | `ATTENDANT` | Altera o status de uma solicitação. |
| `DELETE` | `/request/{id}/delete` | `REQUESTER` | Exclui uma solicitação própria ainda aberta. |
| `GET` | `/dashboard` | `REQUESTER` | Retorna indicadores das solicitações do usuário autenticado. |
| `GET` | `/dashboard/all` | `ATTENDANT` | Retorna indicadores globais. |

### Criar solicitação

`POST /api/v1/request`

```json
{
  "title": "Acesso ao sistema financeiro",
  "description": "Solicito acesso para executar minhas atividades.",
  "category": "IT"
}
```

O título é obrigatório e limitado a 150 caracteres. A descrição e a categoria também são obrigatórias. Categorias aceitas: `IT`, `HR`, `PURCHASING`, `FINANCE` e `INFRASTRUCTURE`.

### Filtrar solicitações

Os filtros são opcionais e podem ser combinados. As datas usam o formato `yyyy-MM-dd`.

```http
GET /api/v1/request/filter?title=acesso&category=IT&status=OPEN&startDate=2026-01-01&endDate=2026-12-31
```

Parâmetros disponíveis: `title` (texto parcial), `category`, `status`, `startDate` e `endDate`. A rota `/request/filter` limita os resultados ao solicitante autenticado; `/request/all` é exclusiva para atendentes e também aceita os mesmos filtros opcionais.

### Alterar status

`PATCH /api/v1/request/{id}/status`

```json
{
  "status": "IN_PROGRESS"
}
```

Status aceitos: `OPEN`, `IN_PROGRESS` e `COMPLETED`.

### Resposta de solicitação

As respostas incluem `id`, `title`, `description`, `requestCategory`, `username`, `createdAt` e `requestStatus`. Erros de validação, autenticação, autorização ou busca podem resultar em respostas HTTP como `400`, `401`, `403` ou `404`.

## Swagger / OpenAPI

Com a aplicação em execução:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Documento OpenAPI: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

No Swagger UI, faça login, copie o valor de `tokenJwt` e use **Authorize** para informar `Bearer <tokenJwt>` e testar as rotas protegidas.

## Testes e build

Os testes usam a configuração ativa da aplicação e precisam de um MySQL acessível, salvo se outra configuração de teste for definida.
- *obs: apenas teste de login foi implementado para conclusão do backend*

**Windows (PowerShell)**

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
```

**Linux/macOS**

```bash
./mvnw test
./mvnw clean package
```

O artefato executável é gerado em `target/api-0.0.1-SNAPSHOT.jar`. Para executá-lo, disponibilize as variáveis necessárias no ambiente:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

A integração contínua está definida em `.github/workflows/ci.yml` e executa `clean package` com Java 21 e MySQL.

## Organização do código

Apenas o README foi criado com auxílio de agentes de IA(Copilot e Gemini) e revisado e ajustado manualmente por mim.

- Autor: lunaovsk

```text
src/main/java/com/portal_interno/api/
├── controller/       # Endpoints REST
├── domain/
│   ├── dto/          # Contratos de entrada e saída
│   ├── model/        # Entidades e tipos do domínio
│   └── repository/   # Repositórios Spring Data JPA
├── infra/
│   ├── config/       # Bootstrap e configuração do Swagger
│   ├── exception/    # Exceções e tratamento global
│   ├── security/     # Configuração do Spring Security
│   └── token/        # JWT e adaptação de usuário
└── service/          # Regras e casos de uso
```
---

## Diagramas e Modelagem

Os diagramas do projeto estão disponíveis em [`docs/diagramas/`](docs/diagramas/):

- [Diagrama de classes](docs-api/classDiagram.jpg)
- [Diagrama de casos de uso](docs-api/casos-de-uso.jpg)
- [Modelo do banco de dados (DER)](docs-api/modelagem_dados.png)
---
## Segurança e implantação

- Use HTTPS em ambientes implantados e forneça segredos por variáveis protegidas do ambiente, nunca pelo código ou repositório.
- Configure senhas de bootstrap e segredo JWT fortes e exclusivos.
- O JWT é assinado, não criptografado;
- A autenticação é stateless por bearer token.
- Restrinja o acesso aos registros de tokens revogados e defina uma política de retenção.
- Este repositório contém somente a API e incluíra Docker Compose  
<br>
*obs: Frontend precisará de configuração e instruções próprias nos respectivo projeto.*
<br>

*Apenas o README foi criado com auxílio de agentes de IA(Copilot e Gemini) e revisado e ajustado manualmente por mim.*
