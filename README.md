# javaEcommerce - Projeto pessoal de E-commerce

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)

[![CI](https://github.com/WenFra005/javaEcommerce/actions/workflows/maven.yml/badge.svg?style=flat-square)](https://github.com/WenFra005/javaEcommerce/actions/workflows/maven.yml)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=WenFra005_javaEcommerce&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=WenFra005_javaEcommerce&metric=coverage)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)

## 🚧 Status do projeto

> ⚠️ **Este projeto está em desenvolvimento**.
> Novas funcionalidades, melhorias e correções estão sendo implementados incrementalmente. Nem todos os microsserviços descritos na arquitetura estão completamente funcionais neste momento.

## Sobre o Projeto

O **javaEcommerce** é uma plataforma de e-commerce baseada em microsserviços, projetada para ser escalável, resiliente e de fácil manutenção. Este repositório é o **monorepositório** que conterá todos os serviços da plataforma, iniciando pelo serviço de Usuários.

O planejamento arquitetural foi baseado no modelo C4, que define contextos e contêineres para garantir uma visão macro e micro do sistema. Os diagramas abaixo ilustram a estrutura planejada e a evolução do projeto.

## Arquitetura do Sistema

### Contexto do Sistema (Nível 1)

Diagrama mostrando as interações entre o sistema de e-commerce e os atores externos (cliente, administradores, fornecedores) e sistemas (gateway de pagamento, transportadora, API de CEP, serviço de e-mail).

![Diagrama de Contexto do Sistema (Nível 1)](https://cdn.jsdelivr.net/gh/WenFra005/javaEcommerce@dev/user-service/docs/diagrams/C4_model_diagrama-Level1-Diagrama_de_contexto.drawio.svg)

### Contêineres (Nível 2)

Diagrama detalhando os microsserviços, banco de dados, API Gateway e message broker que compõem a plataforma.

![Diagrama de Contêineres (Nível 2)](https://cdn.jsdelivr.net/gh/WenFra005/javaEcommerce@dev/user-service/docs/diagrams/C4_model_diagrama-Level2-Diagrama_de_containers.drawio.svg)

## Tecnologias utilizadas

| Camada              | Tecnologia                                                                                                         |
|---------------------|--------------------------------------------------------------------------------------------------------------------|
| **Linguagem**       | ![Tech](https://skillicons.vercel.app/api/svg?i=java&t=Dark)                                                       |
| **Framework**       | ![Tech](https://simpleicons.dev/icons?icons=springboot,spring&theme=dark)                                          |
| **Segurança**       | ![Tech](https://simpleicons.dev/icons?icons=springsecurity&theme=dark)                                             |
| **Persistência**    | ![Tech](https://skillicons.vercel.app/api/svg?i=hibernate&t=Dark)                                                  |
| **Banco de Dados**  | ![Tech](https://skillicons.vercel.app/api/svg?i=postgresql&t=Dark)                                                 |
| **Migrações**       | ![Tech](https://simpleicons.dev/icons?icons=flyway&theme=dark)                                                     |
| **Documentação**    | ![Tech](https://simpleicons.dev/icons?icons=swagger&theme=dark)                                                    |
| **Testes**          | ![Tech](https://simpleicons.dev/icons?icons=junit5&theme=dark)                                                     |
| **CI/CD**           | ![Tech](https://skillicons.vercel.app/api/svg?i=githubactions&t=Dark)                                              |
| **Containerização** | ![Tech](https://skillicons.vercel.app/api/svg?i=docker&t=Dark)                                                     |

## O que já foi implementado (Fase 1)

- [x] **Serviço de usuários**
  - [x] Cadastro de Pessoa Física (PF) e Pessoa Jurídica (PJ)
  - [x] Autenticação JWT (access + refresh token com rotação)
  - [x] Logout (revogação de tokens)
  - [x] Controle de perfis (`ADMIN`, `CLIENTE`, `VENDEDOR`, `FORNECEDOR`)
  - [x] Atualização e deleção de contas
  - [x] Validação de CPF/CNPJ (`@CPF` / `@CNPJ`)
- [x] **Testes**
  - [x] Testes unitários e de integração (repositórios, serviços, controllers)
  - [x] Testcontainers com PostgreSQL real
  - [x] Cobertura de testes monitorada no SonarCloud
- [x] **CI/CD**
  - [x] GitHub Actions (build, testes, análise)
  - [x] SonarCloud (qualidade de código)
  - [x] CodeQL (segurança)
- [x] **Infraestrutura**
  - [x] Dockerização do serviço (imagem otimizada com `jlink`)
  - [x] Docker Compose (aplicação + PostgreSQL)
  - [x] Documentação Swagger/OpenAPI

## Planejamento e Roteiro de Desenvolvimento

### Fase 1 - Fundação (✅️ Concluído)

- [x] Estruturação do monorepositório
- [x] Implementação do Serviço de Usuários
- [x] Autenticação JWT com refresh token
- [x] Testes automatizados
- [x] Dockerização
- [x] CI/CD com SonarCloud e CodeQL

### Fase 2 - Produtos e Catálogo (📝 Planejado)

- [ ] Implementação do Serviço de Produtos
- [ ] CRUD de produtos, categorias e estoque
- [ ] Integração com API de CEP para cálculo de frete

### Fase 3 - Pedidos e Pagamentos (📝 Planejado)

- [ ] Implementação do Serviço de Pedidos
- [ ] Implementação do Serviço de Pagamentos
- [ ] Integração com gateway de pagamento
- [ ] Webhooks para atualização de status

### Fase 4 - Notificações e Comunicação (📝 Planejado)

- [ ] Implementação do Serviço de Notificações
- [ ] Integração com serviço de e-mail/SMS
- [ ] Message Broker (RabbitMQ) para eventos assíncronos

### Fase 5 - Infraestrutura (📝 Planejado)

- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Orquestração com Docker Compose
- [ ] Configuração de logs estruturados
- [ ] Monitoramento (Prometheus + Grafana)

## Como executar o projeto

### Pré-requisitos

- **Java 25** (ou superior)
- **Maven 3.9+**
- **Docker** e **Docker Compose**
- **PostgreSQL 15+** (o Docker Compose sobe automaticamente)

### Passo a passo

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/WenFra005/javaEcommerce.git
   cd javaEcommerce/user-service
   ```

2. **Configure as variáveis de ambiente** (crie um arquivo `.env` na raiz do serviço ou coloque no arquivo `launch.json` se estiver usando o VS Code):

   ```properties
   DB_USER=user
   DB_PASSWORD=password
   NAME_DB=ecommerce
   DB_PORT=5433
   APP_PORT=8080

   # JWT
   JWT_SECRET=minhaChaveSuperSecreta1234567890
   JWT_EXPIRATION_MS=900000
   JWT_REFRESH_EXPIRATION_MS=604800000

   # Admin
   ADMIN_EMAIL=admin@email.com
   ADMIN_PASSWORD=admin123
   ```

3. **Suba o ambiente com Docker Compose:**

    ```bash
    docker-compose up -d
    ```

4. **Acesse a documentação Swagger:**

   - **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
   - **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`
  
## Como executar os testes

```bash
# Executar todos os testes
mvn clean verify

# Executar apenas testes unitários
mvn test

# Gerar relatório de cobertura (JaCoCo)
mvn clean verify
# Abra target/site/jacoco/index.html
```

Se estiver usando a IDE VS Code, você pode utilizar a aba de testes onde a própria IDE executará os testes e exibir um relatório de cobertura de forma automática e nativa.

## Como Contribuir

Contribuições são bem-vindas.

1. **Abra uma issue** descrevendo o que pretende fazer (bug fix, feature, etc.).
2. **Faça um fork** do repositório.
3. **Crie uma branch** com um nome descritivo:

   ```bash
   git checkout -b feat/minha-feature/seu-usuário
   ```

4. **Siga o padrão de commits** ([Conventional Commits](https://www.conventionalcommits.org/)) ([Conventional Commits PT-BT](https://github.com/iuricode/padroes-de-commits)):

    ```bash
    git commit -m "feat: adiciona endpoint para buscar PF por CPF"
    ```

5. **Abra um Pull Request** seguindo o template disponível.

### Diretrizes

- ✅ Testes devem passar (`mvn clean verify`).
- ✅ Quality Gate do SonarCloud deve estar verde.
- ✅ Código deve seguir as boas práticas do projeto.
- ✅ Documentação deve ser atualizada quando necessário.
  
## Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes
