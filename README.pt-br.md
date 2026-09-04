# Phantom Room JavBot

[English](README.md)

Um bot Discord em Java, limpo e modular, focado no gerenciamento de servidores, canais de voz temporários, tickets e organização de gameplays.

> **Status do projeto:** Em desenvolvimento

## Visão Geral

O Phantom Room JavBot é um bot Discord desenvolvido em Java com o objetivo de fornecer funcionalidades automatizadas para gerenciamento de servidores, com foco em canais de voz, tickets e sessões de jogos.

O bot possui um sistema de canais de voz temporários onde os usuários podem receber automaticamente suas próprias salas de voz, ajudando a manter os servidores Discord organizados.

O projeto também possui um sistema de gerenciamento de tickets e armazenamento persistente utilizando MongoDB, permitindo que configurações e dados dos sistemas continuem disponíveis mesmo após o bot ser reiniciado.

O sistema de gerenciamento de gameplays também está em desenvolvimento, permitindo que os usuários agendem sessões de jogos, gerenciem participantes e criem canais de voz privados para cada gameplay.

O projeto utiliza uma arquitetura modular, permitindo que novos sistemas e comandos sejam adicionados ao longo do desenvolvimento.

## Status Atual

O projeto está atualmente em desenvolvimento.

O repositório está sendo construído de forma incremental, com cada branch representando uma etapa ou funcionalidade específica do desenvolvimento.

### Implementado

* Estrutura inicial do projeto
* Configuração do bot Discord
* Configuração de variáveis de ambiente
* Arquitetura de comandos
* Sistema de canais de voz temporários
* Sistema de configuração do servidor
* Gerenciamento de permissões
* Sistema de tickets
* Integração com banco de dados MongoDB
* Persistência das configurações do servidor

### Em Desenvolvimento

* Permissões de comandos

## Principais Funcionalidades

### Canais de Voz Temporários

O bot cria e gerencia automaticamente canais de voz temporários para os usuários, fornecendo salas isoladas enquanto estiverem sendo utilizadas.

### Sistema de Tickets

O sistema de tickets permite que membros do servidor criem tickets de suporte através de uma interface interativa.

Os tickets são organizados automaticamente utilizando categorias configuradas, com permissões gerenciadas para garantir que apenas usuários autorizados tenham acesso a cada ticket.

### Gerenciamento de Gameplays

O sistema de gerenciamento de gameplays permitirá que os membros do servidor organizem sessões de jogos diretamente através do bot.

Os usuários poderão informar:

* Nome do jogo
* Quantidade máxima de jogadores
* Horário agendado

O bot publicará a gameplay em um canal configurado e criará um canal de voz privado para a sessão.

Os participantes poderão entrar ou sair da gameplay através de controles interativos. Os usuários que entrarem receberão automaticamente acesso ao canal de voz correspondente.

O sistema também será responsável pelo gerenciamento do limite de jogadores e do ciclo de vida da gameplay.

### Banco de Dados

O bot utiliza MongoDB para armazenar de forma persistente informações importantes dos sistemas e configurações dos servidores.

O armazenamento em banco de dados permite que as configurações e informações continuem disponíveis mesmo após o bot ser reiniciado.

## Tecnologias

| Tecnologia     | Finalidade                            |
| -------------- | ------------------------------------- |
| Java 17+       | Linguagem principal                   |
| JDA            | Integração com a API do Discord       |
| MongoDB        | Armazenamento persistente             |
| Maven / Gradle | Gerenciamento de build e dependências |

## Configuração

O bot utiliza variáveis de ambiente para dados de configuração sensíveis.

Crie um arquivo `.env` na raiz do projeto:

```env
DISCORD_TOKEN=seu_token_do_bot_aqui
PREFIX=seu_prefixo_aqui
MONGODB_URI=sua_string_de_conexao_mongodb
```

Nunca faça commit do token do bot, credenciais do banco de dados ou outras informações sensíveis no repositório.

## Intents do Discord

O bot requer os seguintes intents habilitados no Discord Developer Portal:

* `SERVER MEMBERS INTENT`
* `MESSAGE CONTENT INTENT`
* `GUILD VOICE STATES`

## Desenvolvimento

O projeto está sendo desenvolvido de forma incremental através de branches separadas do Git.

A branch `base-config` contém a configuração inicial do projeto e serve como base para o desenvolvimento das funcionalidades seguintes.

Conforme novas funcionalidades são implementadas, elas serão desenvolvidas em branches dedicadas e posteriormente integradas ao projeto principal.

## Roadmap

* [x] Configuração inicial do projeto
* [x] Arquitetura de comandos
* [x] Sistema de canais de voz temporários
* [x] Sistema de configuração do servidor
* [x] Gerenciamento de permissões
* [x] Sistema de tickets
* [x] Integração com MongoDB
* [x] Persistência de configurações
* [x] Agendamento de gameplays
* [x] Gerenciamento de participantes
* [ ] Funcionalidades adicionais de gerenciamento do servidor
