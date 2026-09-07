# javaEcommerce - Projeto pessoal de E-commerce

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)

[![CI](https://github.com/WenFra005/javaEcommerce/actions/workflows/maven.yml/badge.svg?style=flat-square)](https://github.com/WenFra005/javaEcommerce/actions/workflows/maven.yml)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=WenFra005_javaEcommerce&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=WenFra005_javaEcommerce&metric=coverage)](https://sonarcloud.io/summary/new_code?id=WenFra005_javaEcommerce)

## 🚧 Status do projeto
>
> ⚠️ **Este projeto está em desenvolvimento**.
> Novas funcionalidades, melhorias e correções estão sendo implementados incrementalmente. Nem todos os microsserviços descritos na arquitetura estão complementamente funcionais neste momento.

## Sobre o Projeto

O **javaEcommerce** é uma plataforma de e-commerce baseada em microsserviços, projetada para ser escalável, resiliente e de fácil manutenção. Este repositório é o **monorepositório** que conterá todos os serviços da plataforma, iniciando pelo serviço de Usuários.

O planejamento arquitetural foi baseado no modelo C4, que define contextos e contêneires para garantir uma visão macro e micro do sistema. Os diagramas abaixo ilustram a estrutura planejada e a evolução do projeto.

## Arquiterura do Sistema

### Contexto do Sistema (Nível 1)

Diagrama mostrando as interações entre o sistema de e-commerce e os atores externos (cliente, administradores, fornecedores) e sistemas (gateway de pagamento, transportadora, API de CEP, serviço de e-mail).

![Texto Alternativo](https://cdn.jsdelivr.net/gh/WenFra005/javaEcommerce@dev/user-service/docs/diagrams/C4_model_diagrama-Level1-Diagrama_de_contexto.drawio.svg)

### Contêineres (Nível 2)

Diagrama detalhando os microsserviços, banco de dados, API Gateway e message broker que compõem a plataforma.

![Texto Alternativo](https://cdn.jsdelivr.net/gh/WenFra005/javaEcommerce@dev/user-service/docs/diagrams/C4_model_diagrama-Level2-Diagrama_de_containers.drawio.svg)

## Tecnologias utilizadas

| Camada             |  Tecnologia                                                                                                        |
|--------------------|--------------------------------------------------------------------------------------------------------------------|
| **Linguagem**      |![Tech](https://skillicons.vercel.app/api/svg?i=java&t=Dark)                                                        |
| **Framework**      |![Tech](https://simpleicons.dev/icons?icons=springboot,spring&theme=dark)                                           |
| **Segurança**      |![Tech](https://simpleicons.dev/icons?icons=springsecurity&theme=dark)                                              |
| **Persistência**   |![Tech](https://skillicons.vercel.app/api/svg?i=hibernate&t=Dark)                                                   |
| **Banco de Dados** |![Tech](https://skillicons.vercel.app/api/svg?i=postgresql&t=Dark)                                                  |
| **Migrações**      |![Tech](https://simpleicons.dev/icons?icons=flyway&theme=dark)                                                      |
| **Documentação**   |![Tech](https://simpleicons.dev/icons?icons=swagger&theme=dark)                                                     |
| **Testes**         |![Tech](https://simpleicons.dev/icons?icons=junit5&theme=dark)                                                      |
| **CI/CD**          |![Tech](https://skillicons.vercel.app/api/svg?i=githubactions&t=Dark)                                               |
| **Containerização**|![Tech](https://skillicons.vercel.app/api/svg?i=docker&t=Dark)                                                      |

## Planejemaneto e Roteiro de Desenvolvimento

### Fase 1 - Fundação (🛠️ Em Desenvolvimento)

- Estruturação do monorepositório
- Implementação do Serviço de Usuários
- Autenticação JWT com refresh token

### Fase 2 - Produtos e Catálogo (📝 Planejado)

- Implementação do Serviço de Produtos
- CRUD de produtos, categorias e estoque

### Fase 3 - Pedidos e Pagamentos (📝 Planejado)

- Implementação do Serviço de Pedidos
- Implementação do Serviços de Pagamentos
- Integração com gateway de pagamento
- Webhooks para atualização de status

### Fase 4 - Notificações e Comunicação (📝 Planejado)

- Implementação do Serviço de Notificações
- Integração com serviço de e-mail/SMS
- Message Broker (RabbitMQ) para eventos assíncronos

### Fase 5 - Infraestrutura (📝 Planejado)

- API Gateway (Spring Cloud Gateway)
- Containerização com Docker
- Orquestração com docker-compose
- Configuração de logs estruturados
- Monitoramento

## Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes
