# Phantom Room JavBot

[English](README.md)

Um bot modular e organizado para Discord, desenvolvido em Java e focado no gerenciamento de canais de voz temporários.

> **Status do projeto:** Em desenvolvimento

## Visão geral

O Phantom Room JavBot é um bot para Discord desenvolvido em Java com o objetivo de fornecer um sistema automatizado de canais de voz temporários.

O conceito principal é simples: quando um usuário entra em um canal de voz definido como gatilho, o bot cria automaticamente uma sala temporária para ele. Quando a sala deixa de ser utilizada, ela pode ser removida automaticamente, mantendo o servidor organizado e evitando o acúmulo de canais desnecessários.

O projeto está sendo desenvolvido com uma arquitetura modular, permitindo a adição de novos comandos e funcionalidades ao longo do desenvolvimento.

## Status atual

O projeto está atualmente em desenvolvimento.

O repositório está sendo construído de forma incremental, com cada branch representando uma etapa ou funcionalidade específica do desenvolvimento.

### Implementado

* Estrutura inicial do projeto
* Configuração básica do bot Discord
* Configuração de variáveis de ambiente
* Estrutura inicial do sistema de canais de voz

### Em desenvolvimento

* Criação de canais de voz temporários
* Gerenciamento dos canais temporários
* Sistema de comandos
* Comandos de configuração do servidor

### Planejado

* Gerenciamento de permissões
* Personalização dos nomes dos canais temporários
* Controles específicos para usuários
* Funcionalidades adicionais de gerenciamento e moderação

## Tecnologias

| Tecnologia     | Finalidade                            |
| -------------- | ------------------------------------- |
| Java 17+       | Linguagem principal                   |
| JDA            | Integração com a API do Discord       |
| Maven / Gradle | Build e gerenciamento de dependências |

## Configuração

O bot utiliza variáveis de ambiente para armazenar informações sensíveis de configuração.

Crie um arquivo `.env` na raiz do projeto:

```env
DISCORD_TOKEN=seu_token_aqui
PREFIX=seu_prefixo_aqui
```

Nunca envie o token do bot ou outras credenciais sensíveis para o repositório.

## Intents do Discord

O bot requer os seguintes intents habilitados no Discord Developer Portal:

* `SERVER MEMBERS INTENT`
* `MESSAGE CONTENT INTENT`
* `GUILD VOICE STATES`

## Desenvolvimento

O projeto está sendo desenvolvido de forma incremental através de branches separadas no Git.

A branch `base-config` contém a configuração inicial do projeto e serve como base para as próximas etapas do desenvolvimento.

À medida que novas funcionalidades são implementadas, elas serão desenvolvidas em branches específicas e posteriormente integradas ao projeto principal.

## Roadmap

* [x] Configuração inicial do projeto
* [ ] Arquitetura do sistema de comandos
* [ ] Criação de canais de voz temporários
* [ ] Remoção de canais de voz temporários
* [ ] Configuração dos canais
* [ ] Gerenciamento de permissões
* [ ] Funcionalidades adicionais de gerenciamento do servidor

