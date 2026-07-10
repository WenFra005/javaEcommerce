# javaEcommerce - Projeto pessoal de E-commerce

## 🚧 Status do projeto
> ⚠️ **Este projeto está em desenvolvimento**.

> Novas funcionalidades, melhorias e correções estão sendo implementados incrementalmente. Nem todos os microsserviços descritos na arquitetura estão complementamente funcionais neste momento.


## Sobre o Projeto

O **javaEcommerce** é uma plataforma de e-commerce baseada em microsserviços, projetada para ser escalável, resiliente e de fácil manutenção. Este repositório é o **monorepositório** que conterá todos os serviços da plataforma, iniciando pelo serviço de Usuários.

O planejamento arquitetural foi baseado no modelo C4, que define contextos e contêneires para garantir uma visão macro e micro do sistema. Os diagramas abaixo ilustram a estrutura planejada e a evolução do projeto.

## Arquiterura do Sistema

### Contexto do Sistema (Nível 1)
Diagrama mostrando as interações entre o sistema de e-commerce e os atores externos (cliente, administradores, fornecedores) e sistemas (gateway de pagamento, transportadora, API de CEP, serviço de e-mail).

<img src="docs/diagrams/C4_model_diagrama-Level1-Diagrama de contexto.drawio.svg" alt="Level 1 - Diagrama de contexto">

### Contêineres (Nível 2)
Diagrama detalhando os microsserviços, banco de dados, API Gateway e message broker que compõem a plataforma.


<img src="docs/diagrams/C4_model_diagrama-Level2-Diagrama de containers.drawio.svg" alt="Level 2 - Diagrama de containers">


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